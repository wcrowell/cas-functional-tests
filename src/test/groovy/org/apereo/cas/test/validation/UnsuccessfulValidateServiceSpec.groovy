package org.apereo.cas.test.validation

import static groovyx.net.http.ContentType.*

import org.apereo.cas.test.common.CommonGebSpec;

class UnsuccessfulValidateServiceSpec extends CommonGebSpec {
	private static final FOO = 'foo'
	
	def setup() {
		super
	}

	def "CAS 2.0 unsuccessful validation (bad ticket, wrong service)"() {
		given:

		client.contentType = XML
		client.headers = [Accept : 'application/xml']
				
		// Try to validate a bad service ticket
		def response = client.get( path : "/" + properties."cas.context.root" + "/serviceValidate",
			query : [ service: "$FOO", ticket: "$FOO"]) { resp, xml ->
				assert resp.status == 200
				assert xml.authenticationFailure.@code == "ServiceManagement: Unauthorized Service Access. Service [$FOO] is not found in service registry."
			}

		// Use valid service ticket to get access to a service that does not match what the ticket was issued for.
		def serviceTicket = getServiceTicket('protected-web-app')

		client.contentType = XML
		client.headers = [Accept : 'application/xml']

		response = client.get( path : "/" + properties."cas.context.root" + "/serviceValidate",
			query : [ service: "$baseUrl/cas-services/", ticket: "$serviceTicket"]) { resp, xml ->
				assert resp.status == 200
				assert xml.authenticationFailure.@code == 'INVALID_SERVICE'
			}
    }
}
