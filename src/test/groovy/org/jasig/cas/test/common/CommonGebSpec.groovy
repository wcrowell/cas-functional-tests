package org.jasig.cas.test.common
import geb.spock.GebSpec
import pages.LoginPage
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import groovyx.net.http.RESTClient

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

	def String getServiceTicket(String service) {
		def client = new RESTClient(browser.config.baseUrl)
		// If using SSL, then this method must be called or else a javax.net.ssl.SSLHandshakeException will result due to a self-signed certificate in /etc/jetty/keystore.
		client.ignoreSSLIssues()

		// Get a ticket granting ticket
		def ticket = getTicketGrantingTicket(client);

		// Get a service ticket	using the ticket granting ticket
		def respSt = client.post( path : "/" + properties."cas.context.root" + "/v1/tickets/$ticket",
			body : [ service: "$baseUrl/$service/" ],
			requestContentType : "application/x-www-form-urlencoded" )

		assert respSt.status == 200
		def serviceTicket = respSt.data.text

		println "serviceTicket: $serviceTicket"
		
		return serviceTicket
	}	
	
	def String getTicketGrantingTicket(RESTClient client) {
		// Get a ticket granting ticket
		def respTgt = client.post( path : "/" + properties."cas.context.root" + "/v1/tickets",
			body : [ username: properties.username, password: properties.password ],
			requestContentType : "application/x-www-form-urlencoded" )

		assert respTgt.status == 201
		def url = respTgt.headers.Location
		def String[] nodes = url.split("/")
		def ticket = nodes.getAt(nodes.length - 1)
		
		println "ticket: $ticket"
		
		return ticket
	}
}
