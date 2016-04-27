package pages

import geb.Page


class AdminPage extends Page {
	// url will be overwritten in getPageUrl() below
	static url = "/cas-services"
	
	static at = {
		title == "Services Management"
	}

	static content = {
		logoutButton { $("a", id: "logoutUrlLink") }
	}
	
	String getPageUrl() {
		def properties = browser.config.rawConfig.properties
		println "88888"
		println "$properties.service"
		println "99999"
		url = "/$properties.service"
	}
}
