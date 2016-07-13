package org.apereo.cas.test.validation

import static groovyx.net.http.ContentType.*

import org.apereo.cas.test.common.CommonGebSpec;

class MultiLevelProxySpec extends CommonGebSpec {
	def setup() {
		super
	}

	def "multi-level proxy"() {
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

		// Validate the service ticket and get the proxy granting ticket IOU
		def proxyGrantingTicketIou2
		def proxyGrantingTicketUrl2
		respSt = client.get( path : "/" + properties."cas.context.root" + "/proxyValidate",
			query : [ service: "$baseUrl/protected-web-app/", ticket: "$proxyTicket", pgtUrl: "$baseUrl/protected-web-app/proxyUrl"])  { resp, xml ->
				assert resp.status == 200
				proxyGrantingTicketIou2 = xml.authenticationSuccess.proxyGrantingTicket
				proxyGrantingTicketUrl2 = xml.authenticationSuccess.proxies.proxy[0]
			}
			
		println "proxyGrantingTicketIou2: $proxyGrantingTicketIou2"
		println "proxyGrantingTicketUrl2: $proxyGrantingTicketUrl2"
		
		// Correlate PGTIOU with PGT
		def proxyGrantingTicket2
		client.contentType = TEXT
		client.headers = [Accept : 'text/plain']
		respSt = client.get( path : "/protected-web-app/proxyUrl",
			query : [ requestPgtId: "true", pgtIou: "$proxyGrantingTicketIou2"])  { resp, xml ->
				assert resp.status == 200
				proxyGrantingTicket2 = xml.text
			}

		println "proxyGrantingTicket2: $proxyGrantingTicket2"
		
		// Get the proxy ticket
		def proxyTicket2
		client.contentType = XML
		client.headers = [Accept : 'application/xml']
		respSt = client.get( path : "/" + properties."cas.context.root" + "/proxy",
			query : [ targetService: "$baseUrl/protected-web-app/", pgt: "$proxyGrantingTicket2"])  { resp, xml ->
				assert resp.status == 200
				proxyTicket2 = xml.proxySuccess.proxyTicket
			}
		
		println "proxyTicket2: $proxyTicket2"

		// Get the proxy granting ticket URL
		def proxyGrantingTicketUrl
		respSt = client.get( path : "/" + properties."cas.context.root" + "/proxyValidate",
			query : [ service: "$baseUrl/protected-web-app/", ticket: "$proxyTicket2"])  { resp, xml ->
				assert resp.status == 200
				assert xml.authenticationSuccess.proxies.proxy[0] == "$baseUrl/protected-web-app/proxyUrl"
				// TODO: Will come back later and add a 2nd test app.  This is sufficient now for testing.
				assert xml.authenticationSuccess.proxies.proxy[1] == "$baseUrl/protected-web-app/proxyUrl"
			}
    }
}
