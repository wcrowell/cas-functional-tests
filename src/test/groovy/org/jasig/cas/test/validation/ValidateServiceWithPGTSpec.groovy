package org.jasig.cas.test.validation

import static groovyx.net.http.ContentType.*
import org.jasig.cas.test.common.CommonGebSpec

class ValidateServiceWithPGTSpec extends CommonGebSpec {
	def setup() {
		super
	}

	def "CAS 2.0 validation, acquire proxy-granting ticket, proxy authentication"() {
		given:

		// Get a service ticket	using the ticket granting ticket
		def serviceTicket = getServiceTicket("protected-web-app")

		client.contentType = XML
		client.headers = [Accept : 'application/xml']
		
		// Validate the service ticket and get the proxy granting ticket IOU
		def proxyGrantingTicketIou
		def respSt = client.get( path : "/" + properties."cas.context.root" + "/serviceValidate", 
			query : [ service: "$baseUrl/protected-web-app/", ticket: "$serviceTicket", pgtUrl: "$baseUrl/protected-web-app/proxyUrl"])  { resp, xml ->
				assert resp.status == 200
				proxyGrantingTicketIou = xml.authenticationSuccess.proxyGrantingTicket
			}
			
		println "proxyGrantingTicketIou: $proxyGrantingTicketIou"
		
		// Correlate PGTIOU with PGT
		def proxyGrantingTicket
		client.contentType = TEXT
		client.headers = [Accept : 'text/plain']
		respSt = client.get( path : "/protected-web-app/proxyUrl",
			query : [ requestPgtId: "true", pgtIou: "$proxyGrantingTicketIou"])  { resp, xml ->
				assert resp.status == 200
				proxyGrantingTicket = xml.text
			}

		println "proxyGrantingTicket: $proxyGrantingTicket"

		client.contentType = XML
		client.headers = [Accept : 'application/xml']

		// Get the proxy ticket
		def proxyTicket
		respSt = client.get( path : "/" + properties."cas.context.root" + "/proxy",
			query : [ targetService: "$baseUrl/protected-web-app/", pgt: "$proxyGrantingTicket"])  { resp, xml ->
				assert resp.status == 200
				proxyTicket = xml.proxySuccess.proxyTicket
			}
		
		println "proxyTicket: $proxyTicket"

		// Get the proxy granting ticket URL
		def proxyGrantingTicketUrl
		respSt = client.get( path : "/" + properties."cas.context.root" + "/proxyValidate",
			query : [ service: "$baseUrl/protected-web-app/", ticket: "$proxyTicket"])  { resp, xml ->
				assert resp.status == 200
				proxyGrantingTicketUrl = xml.authenticationSuccess.proxies.proxy[0]
			}
		
		println "proxyGrantingTicketUrl: $proxyGrantingTicketUrl"

		// Ensure the proxy ticket cannot be used again.
		respSt = client.get( path : "/" + properties."cas.context.root" + "/proxyValidate",
			query : [ service: "$baseUrl/protected-web-app/", ticket: "$proxyTicket", reload: "true"])  { resp, xml ->
				assert resp.status == 200
				println "Proxy ticket status is now: " + xml.authenticationFailure.@code
				assert xml.authenticationFailure.@code == 'INVALID_TICKET'
			}
    }
}
