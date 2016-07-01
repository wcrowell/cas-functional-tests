package org.jasig.cas.test.validation
import pages.LoginPage
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.jasig.cas.test.common.CommonGebSpec

class ValidateSpec extends CommonGebSpec {
	def setup() {
		super
	}

	def "CAS 1.0 validation"() {
		given:

		def serviceTicket = getServiceTicket("protected-web-app")
		
		// Validate the service ticket
		go "/" + properties."cas.context.root" + "/validate?service=$baseUrl/protected-web-app/&ticket=$serviceTicket"

		println "result: $driver.pageSource"

		// Appears to be a difference the way HtmlUnitDriver behaves from ChromeDriver
	    if (driver instanceof HtmlUnitDriver) {	
        	assert driver.pageSource == "yes\ncasuser\n"
		} else {
			assert $().text() == "yes\ncasuser"
		}
    }
}
