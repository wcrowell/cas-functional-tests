import groovyx.net.http.*

class UnsuccessfulValidationAtValidateSpec extends CommonGebSpec {
	def setup() {
		super
	}

	def "CAS 2.0 unsuccessful validation at /validate"() {
		given:

		// Get a service ticket	using the ticket granting ticket
		def serviceTicket = getServiceTicket("protected-web-app")

		def client = new HTTPBuilder(browser.config.baseUrl)

		// If using SSL, then this method must be called or else a javax.net.ssl.SSLHandshakeException will result due to a self-signed certificate in /etc/jetty/keystore.
		client.ignoreSSLIssues()

		client.contentType = ContentType.XML
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
		client.contentType = ContentType.TEXT
		client.headers = [Accept : 'text/plain']
		respSt = client.get( path : "/protected-web-app/proxyUrl",
			query : [ requestPgtId: "true", pgtIou: "$proxyGrantingTicketIou"])  { resp, xml ->
				assert resp.status == 200
				proxyGrantingTicket = xml.text
			}

		println "proxyGrantingTicket: $proxyGrantingTicket"

		client.contentType = ContentType.XML
		client.headers = [Accept : 'application/xml']

		// Get the proxy ticket
		def proxyTicket
		respSt = client.get( path : "/" + properties."cas.context.root" + "/proxy",
			query : [ targetService: "$baseUrl/protected-web-app/", pgt: "$proxyGrantingTicket"])  { resp, xml ->
				assert resp.status == 200
				proxyTicket = xml.proxySuccess.proxyTicket
			}
		
		println "proxyTicket: $proxyTicket"

		client.contentType = ContentType.TEXT
		
		// Submit the proxy ticket to /validate which should not allow proxy tickets.
		respSt = client.get( path : "/" + properties."cas.context.root" + "/validate", 
			query : [ service: "$baseUrl/protected-web-app/", ticket: "$proxyTicket"])  { resp, xml ->
				assert resp.status == 200
				assert xml.text.trim() == 'no'
			}
	}
}
