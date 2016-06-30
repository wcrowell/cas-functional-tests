import groovyx.net.http.*

class ValidateFailureSpec extends CommonGebSpec {
	def setup() {
		super
	}

	def "CAS 1.0 validation failure (bad ticket, wrong service)"() {
		given:

		def client = new HTTPBuilder(browser.config.baseUrl)

		// If using SSL, then this method must be called or else a javax.net.ssl.SSLHandshakeException will result due to a self-signed certificate in /etc/jetty/keystore.
		client.ignoreSSLIssues()

		client.contentType = ContentType.TEXT
		client.headers = [Accept : 'application/xml']
		
		// Validate a non-existing service and bad service ticket
		def respSt = client.get( path : "/" + properties."cas.context.root" + "/validate",
			query : [ service: "$baseUrl/foo/", ticket: 'foo'])  { resp, xml ->
				assert resp.status == 200
				assert xml.text.trim() == 'no'
			}

		// Get a service ticket
		def serviceTicket = getServiceTicket("protected-web-app")
		
		// Validate a non-existing service and valid service ticket 
		respSt = client.get( path : "/" + properties."cas.context.root" + "/validate",
			query : [ service: "$baseUrl/bar/", ticket: "$serviceTicket"])  { resp, xml ->
				assert resp.status == 200
				assert xml.text.trim() == 'no'
			}
    }
}
