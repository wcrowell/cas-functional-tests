package org.apereo.cas.test.pages;

import geb.Page

class LoginPage extends Page {
	// url will be overwritten in getPageUrl() below
	static url = "/cas/login"
	
	static at = {
		title == "CAS – Central Authentication Service"
	}

	static content = {
		loginButton { $("input", type: "submit") }
		username { $("input", name: "username") }
		password { $("input", name: "password") }
	}
	
	def loginAs(user, pass) {
		username = user
		password = pass
		loginButton.click()
	}
	
	// Function must be overriden or it will interfere with other tests because url is static.
	String getPageUrl() {
		def properties = browser.config.rawConfig.properties
		url = "/" + properties."cas.context.root" + "/login"
	}
}

