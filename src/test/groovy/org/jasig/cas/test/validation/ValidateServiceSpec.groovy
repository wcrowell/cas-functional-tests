package org.jasig.cas.test.validation

import static groovyx.net.http.ContentType.*
import org.jasig.cas.test.common.CommonGebSpec

class ValidateServiceSpec extends CommonGebSpec {
	def setup() {
		super
	}

	def "CAS 2.0 validation"() {
		given:

		def serviceTicket = getServiceTicket("protected-web-app")

		client.contentType = XML
		client.headers = [Accept : 'application/xml']
				
		// Validate the service ticket
		def response = client.get( path : "/" + properties."cas.context.root" + "/serviceValidate",
			query : [ service: "$baseUrl/protected-web-app/", ticket: "$serviceTicket"]) { resp, xml ->
				assert resp.status == 200
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
