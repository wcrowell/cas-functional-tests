package org.apereo.cas.test.pages

class LoginPageWithService extends LoginPage {
	String getPageUrl() {
		def properties = browser.config.rawConfig.properties
		def baseURL = browser.config.baseUrl
		url = "/" + properties."cas.context.root" + "/login?service=$baseURL/$properties.service"
	}
}
