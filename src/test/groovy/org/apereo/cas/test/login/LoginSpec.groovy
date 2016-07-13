package org.apereo.cas.test.login
import org.apereo.cas.test.common.CommonGebSpec
import org.apereo.cas.test.pages.LoginPage;;

class LoginSpec extends CommonGebSpec {
	static loginSuccessful = "Log In Successful"

	def setup() {
		super
		to LoginPage
	}

	def "Correct credentials, no service, SSO test"() {
		given:
        at LoginPage

        when: "login using provided credentials"
		loginAs(properties.username, properties.password)
		
		// Verify Ticket Granting Cookie presence
		verifyCookie("TGC")

        then: "Verify confirmation page is displayed"
        at LoginPage
		assert $("div", id: "msg").$("h2").text() == loginSuccessful

		to LoginPage
		assert $("div", id: "msg").$("h2").text() == loginSuccessful
    }
}
