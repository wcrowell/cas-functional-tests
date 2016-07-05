package org.jasig.cas.test.validation

import static groovyx.net.http.ContentType.*
import org.jasig.cas.test.common.CommonGebSpec

class ValidateSpec extends CommonGebSpec {
	def setup() {
		super
	}

	def "CAS 1.0 validation"() {
		given:

		// Get a service ticket	using the ticket granting ticket
		def serviceTicket = getServiceTicket("protected-web-app")

		client.contentType = TEXT
		client.headers = [Accept : 'application/xml']
		
		// Validate the service ticket
		def respSt = client.get( path : "/" + properties."cas.context.root" + "/validate",
			query : [ service: "$baseUrl/protected-web-app/", ticket: "$serviceTicket"])  { resp, xml ->
				assert resp.status == 200
				assert xml.text == "yes\ncasuser\n"
			}
    }
}
