package org.jasig.cas.test.validation

import static groovyx.net.http.ContentType.*
import org.jasig.cas.test.common.CommonGebSpec

class ValidateFailureSpec extends CommonGebSpec {
	def setup() {
		super
	}

	def "CAS 1.0 validation failure (bad ticket, wrong service)"() {
		given:

		client.contentType = TEXT
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
