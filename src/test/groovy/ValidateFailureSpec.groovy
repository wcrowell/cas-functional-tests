import pages.LoginPage
import org.openqa.selenium.htmlunit.HtmlUnitDriver

class ValidateFailureSpec extends CommonGebSpec {
	def setup() {
		super
	}

	def "CAS 1.0 validation failure (bad ticket, wrong service)"() {
		// Validate a non-existing service and bad service ticket
		go "/" + properties."cas.context.root" + "/validate?service=$baseUrl/foo/&serviceTicket=foo"

		println "result: $driver.pageSource"

		assert driver.pageSource.trim() == "no"

		given:

		def serviceTicket = getServiceTicket("protected-web-app")
		
		// Validate a non-existing service and valid service ticket 
		go "/" + properties."cas.context.root" + "/validate?service=$baseUrl/bar/&ticket=$serviceTicket"
		
		println "result: $driver.pageSource"

		assert driver.pageSource.trim() == "no"
    }
}
