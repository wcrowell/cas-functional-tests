package org.apereo.cas.test.common

import geb.spock.GebSpec
import groovyx.net.http.*

class CommonGebSpec extends GebSpec {
	static def cachedDriver // static variable will store our single driver instance
	static def contextRoot
	def properties = browser.config.rawConfig.properties
	def client = new HTTPBuilder(browser.config.baseUrl)
	def ticketGrantingTicket
	
	def setup() {
		contextRoot = properties."cas.context.root"
		driver = cachedDriver   // each test should use our cached browser instance
		
		// If using SSL, then this method must be called or else a javax.net.ssl.SSLHandshakeException will result due to a self-signed certificate in /etc/jetty/keystore.
		client.ignoreSSLIssues()
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
		// Get a ticket granting ticket
		def ticket = getTicketGrantingTicket();

		client.contentType = ContentType.TEXT
		client.headers = [Accept : 'text/plain']

		def serviceTicket
		// Get a service ticket	using the ticket granting ticket
		def respSt = client.post( path : "/" + properties."cas.context.root" + "/v1/tickets/$ticketGrantingTicket",
			body : [ service: "$baseUrl/$service/" ],
			requestContentType : "application/x-www-form-urlencoded" ) { resp, xml ->
				assert resp.status == 200
				serviceTicket = xml.text
			}

		println "serviceTicket: $serviceTicket"
		
		return serviceTicket
	}	
	
	def getTicketGrantingTicket() {
		client.contentType = ContentType.TEXT
		client.headers = [Accept : 'text/plain']
		
		// Get a ticket granting ticket
		def respTgt = client.post( path : "/" + properties."cas.context.root" + "/v1/tickets",
			body : [ username: properties.username, password: properties.password ],
			requestContentType : "application/x-www-form-urlencoded" ) { resp, xml ->
				assert resp.status == 201
				def url = resp.headers.Location
				def String[] nodes = url.split("/")
				ticketGrantingTicket = nodes.getAt(nodes.length - 1)
			}

		println "ticketGrantingTicket: $ticketGrantingTicket"
	}
	
	def deleteTicketGrantingTicket(String ticket) {
		def restClient = new RESTClient(browser.config.baseUrl)
		// If using SSL, then this method must be called or else a javax.net.ssl.SSLHandshakeException will result due to a self-signed certificate in /etc/jetty/keystore.
		restClient.ignoreSSLIssues()
		
		restClient.contentType = ContentType.TEXT
		restClient.headers = [Accept : 'text/plain']
		
		// Delete a ticket granting ticket
		if (ticket != null && ticket != '') {
			def resp = restClient.delete( path : "/" + properties."cas.context.root" + "/v1/tickets/$ticket")
			assert resp.status == 200
			println "Ticket ID ${resp.data} was deleted."
		} else {
			def resp = restClient.delete( path : "/" + properties."cas.context.root" + "/v1/tickets/$ticketGrantingTicket")
			assert resp.status == 200
			println "Ticket Granting Ticket ID ${resp.data} was deleted."
		}
	}
}
