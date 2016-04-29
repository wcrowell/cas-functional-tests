import pages.LoginPageWithService
import pages.ProtectedWebAppPage
import pages.RenewPage

class RenewSpec extends CommonGebSpec {
	def setup() {
		super
		to ProtectedWebAppPage
	}

    def "Logging in as someone else"() {
		sleep(60000)
 		// Visit /login?service=foo
		given:
        at LoginPageWithService

        when: "submit correct credentials for user A"
		loginAs(properties.usernameb, properties.passwordb)
        // "you should be redirected to foo with a valid service ticket"

		// Visit /login?service=bar&renew=true
		to RenewPage
		
		// Login as user B
		loginAs(properties.usernamec, properties.passwordc)
		
		then: "end"
    }
}
