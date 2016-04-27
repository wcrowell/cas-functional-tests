import pages.AdminPage
import pages.LoginPageWithService
import pages.RenewPage

class RenewSpec extends CommonGebSpec {
	def setup() {
		super
		to LoginPageWithService
	}

    def "Logging in as someone else"() {
        at LoginPageWithService

        when: "submit correct credentials"
		loginAs(properties.username, properties.password)

		String cookieValue = verifyCookie(properties."cookie.name")

        then: "you should be redirected to foo with a valid service ticket"
        at AdminPage
		String gatewayCookieValue = verifyCookie(properties."cookie.name")

		// Visit /login?service=bar&renew=true
		to RenewPage
		
		// Login as user B
		loginAs(properties.usernameb, properties.passwordb)
    }
}
