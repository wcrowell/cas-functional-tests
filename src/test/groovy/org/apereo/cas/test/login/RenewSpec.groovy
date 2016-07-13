package org.apereo.cas.test.login
import org.apereo.cas.test.common.CommonGebSpec
import org.apereo.cas.test.pages.LoginPageWithService;

import groovyx.net.http.URIBuilder

class RenewSpec extends CommonGebSpec {
	def baseUrl = browser.config.baseUrl
	
	def setup() {
		super
		def properties = browser.config.rawConfig.properties
 		// Visit /login?service=foo
		url = "/" + properties."cas.context.root" + "/login?service=$baseUrl/protected-web-app/"
		go url
	}

    def "Logging in as someone else"() {
		given:
        at LoginPageWithService

		// Submit correct credentials for user A
        when: "submit correct credentials for user A"
		loginAs(properties.usernamea, properties.passworda)
		// You should be redirected to foo
		
		def builder = new URIBuilder(browser.driver.currentUrl);
		assert builder.query.ticket != null
		def userATicketId = builder.query.ticket
		println "User A Ticket ID: " + userATicketId

		// Logout of the app to invalidate session and logout of SSO.  
		// Note: This must be done or the renew does not work right.	
		go "/protected-web-app/logout.jsp"
		go "/$contextRoot/logout"
		
		// Visit /login?service=bar&renew=true
		go "/" + properties."cas.context.root" + "/login?service=$baseUrl/protected-web-app/&renew=true"
				
		// You should be prompted for username and password.
		
        at LoginPageWithService
		// Submit correct credentials for user B
		loginAs(properties.usernameb, properties.passwordb)
		
		builder = new URIBuilder(browser.driver.currentUrl);
		assert builder.query.ticket != null
		def userBTicketId = builder.query.ticket
		println "User B Ticket ID: " + userBTicketId

		// You should receive a new TGT and a service ticket for user B. 
		// The TGT cookie for user A should be destroyed.
		assert userATicketId != userBTicketId
		
		then: "end"
    }
}
