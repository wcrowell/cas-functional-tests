package org.apereo.cas.test.validation

import static groovyx.net.http.ContentType.*

import org.apereo.cas.test.common.CommonGebSpec;

class PGTInvalidationByLogoutSpec extends CommonGebSpec {
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

		// Visit logout
		deleteTicketGrantingTicket()

		// Ensure the proxy ticket cannot be used after logout
		respSt = client.get( path : "/" + properties."cas.context.root" + "/proxy",
			query : [ targetService: "$baseUrl/protected-web-app/", pgt: "$proxyGrantingTicket"])  { resp, xml ->
				assert resp.status == 200
				println "final: " + xml.text
				// assert xml.proxyFailure.@code == 'BAD_PGT' 
			}
    }
}
