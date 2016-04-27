import pages.LoginPage
import pages.LoginPageWithService


class BadLoginSpec extends CommonGebSpec {
	static invalidCredentials = "Invalid credentials."

	def setup() {
		super
		to LoginPage
	}

	def "authenticates a bad user"() {
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