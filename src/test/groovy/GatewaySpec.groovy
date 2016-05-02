import pages.AdminPage
import pages.GatewayPage
import pages.LoginPageWithService
import groovyx.net.http.URIBuilder

class GatewaySpec extends CommonGebSpec {
	def setup() {
		super
		to LoginPageWithService
	}

    def "Gateway test"() {
		given: "Visit /login?service=foo"
        at LoginPageWithService

        when: "submit correct credentials"
		loginAs(properties.username, properties.password)

		String cookieValue = verifyCookie(properties."cookie.name")

        then: "you should be redirected to foo with a valid service ticket"
        at AdminPage
		String gatewayCookieValue = verifyCookie(properties."cookie.name")

		// Visit /login?service=bar&gateway=true
		to GatewayPage

		def builder = new URIBuilder(browser.driver.currentUrl);
		println "Ticket ID: " + builder.query.ticket
		assert builder.query.ticket != null
		
		then: "You should be redirected to bar with a valid service ticket"
		at GatewayPage
    }
}
