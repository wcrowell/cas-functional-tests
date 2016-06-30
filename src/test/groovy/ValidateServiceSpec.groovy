import groovyx.net.http.*

class ValidateServiceSpec extends CommonGebSpec {
	def setup() {
		super
	}

	def "CAS 2.0 validation"() {
		given:

		def serviceTicket = getServiceTicket("protected-web-app")

		def client = new HTTPBuilder(browser.config.baseUrl)
		
		// If using SSL, then this method must be called or else a javax.net.ssl.SSLHandshakeException will result due to a self-signed certificate in /etc/jetty/keystore.
		client.ignoreSSLIssues()
		
		//client.contentType = ContentType.TEXT
		client.contentType = ContentType.XML
		client.headers = [Accept : 'application/xml']
				
		// Validate the service ticket
		def response = client.get( path : "/" + properties."cas.context.root" + "/serviceValidate",
			query : [ service: "$baseUrl/protected-web-app/", ticket: "$serviceTicket"]) { resp, xml ->
				assert resp.status == 200
				//def serviceResponse = new XmlSlurper().parse(xml)
				//assert serviceResponse.authenticationSuccess.user == 'casuser'
				assert xml.authenticationSuccess.user == 'casuser'
			} 

		// Make sure the service ticket cannot be used again.
		response = client.get( path : "/" + properties."cas.context.root" + "/serviceValidate",
			query : [ service: "$baseUrl/protected-web-app/", ticket: "$serviceTicket", reload: "true"]) { resp, xml ->
				assert resp.status == 200
				assert xml.authenticationFailure.@code == 'INVALID_TICKET'
			}
    }
}
