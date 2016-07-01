package org.jasig.cas.test.validation

import groovyx.net.http.*
import org.jasig.cas.test.common.CommonGebSpec

class ValidateSpec extends CommonGebSpec {
	def setup() {
		super
	}

	def "CAS 1.0 validation"() {
		given:

		// Get a service ticket	using the ticket granting ticket
		def serviceTicket = getServiceTicket("protected-web-app")

		def client = new HTTPBuilder(browser.config.baseUrl)

		// If using SSL, then this method must be called or else a javax.net.ssl.SSLHandshakeException will result due to a self-signed certificate in /etc/jetty/keystore.
		client.ignoreSSLIssues()

		client.contentType = ContentType.TEXT
		client.headers = [Accept : 'application/xml']
		
		// Validate the service ticket
		def respSt = client.get( path : "/" + properties."cas.context.root" + "/validate",
			query : [ service: "$baseUrl/protected-web-app/", ticket: "$serviceTicket"])  { resp, xml ->
				assert resp.status == 200
				assert xml.text == "yes\ncasuser\n"
			}
    }
}
