package org.apereo.cas.test.pages

import geb.Page
import geb.Browser;

class LogoutPage extends Page {
	// url will be overwritten in getPageUrl() below
	static url = "/cas-services/logout?url=/logout.html"
	
	String getPageUrl() {
		def properties = browser.config.rawConfig.properties
		url = "/$properties.service/logout?url=/logout.html"
	}

	static at = {
		title == "Services Management"
	}
	
	static content = {
		logoutButton { $("a", id: "logoutUrlLink") }
	}
	
	def logout() {
		logoutButton.click()
	}
}
