package org.apereo.cas.test.pages;

import geb.Page

class LoginPageSuccessful extends Page {
	// url will be overwritten in getPageUrl() below
	static url = "/cas/login"
	
	static at = {
		title == "Log In Successful - CAS – Central Authentication Service"
	}
	
	// Function must be overriden or it will interfere with other tests because url is static.
	String getPageUrl() {
		def properties = browser.config.rawConfig.properties
		url = "/" + properties."cas.context.root" + "/login"
	}
}

