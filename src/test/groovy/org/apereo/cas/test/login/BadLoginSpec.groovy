package org.apereo.cas.test.login
import org.apereo.cas.test.common.CommonGebSpec
import org.apereo.cas.test.pages.LoginPage;
import org.apereo.cas.test.pages.LoginPageWithService;;;

class BadLoginSpec extends CommonGebSpec {
	static invalidCredentials = "Invalid credentials."

	def setup() {
		super
		to LoginPage
	}

	def "Incorrect credentials"() {
		given:
		at LoginPageWithService

		when: "login using provided credentials"
		
		loginAs(properties.username, properties."bad.password")
		assert $("div", id: "msg").text() == invalidCredentials
		assert driver.manage().getCookies().contains(properties."cookie.name") == false

		then: "verify login page is displayed again"
		at LoginPageWithService
	}
}