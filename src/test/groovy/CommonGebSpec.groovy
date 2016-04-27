import geb.spock.GebSpec
import pages.LoginPage

class CommonGebSpec extends GebSpec {
	static def cachedDriver // static variable will store our single driver instance
	static def contextRoot
	def properties = browser.config.rawConfig.properties
	
	def setup() {
		contextRoot = properties."cas.context.root"
		driver = cachedDriver   // each test should use our cached browser instance
	}

	// Browser is being reused, thus, logout from the application after
	// each test to have fresh browser state on every test
	def cleanup() {
		if ($(id: "logout").isDisplayed()) {
			$(id: "logout").click()
			waitFor { $(id: "inputName").isDisplayed() }
		}
		driver.manage().deleteAllCookies()
		
		// User must logout of the CAS SSO session.
		browser.go("/$contextRoot/logout")
	}
	
	def String verifyCookie(String cookieName) {
		def cookie = driver.manage().getCookieNamed(cookieName)
		assert cookie
		String cookieValue = cookie.getValue()
		assert cookieValue
		
		// If the cookie exists, then return the value.
		return cookieValue
	}
}
