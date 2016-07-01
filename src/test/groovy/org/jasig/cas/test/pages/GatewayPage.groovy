package org.jasig.cas.test.pages

class GatewayPage extends LoginPage {
	static at = {
		title == "CAS Example Java Web App"
	}

	String getPageUrl() {
		def properties = browser.config.rawConfig.properties
		def baseURL = getBrowser().getConfig().getBaseUrl()
		url = "/" + properties."cas.context.root" + "/login?service=$baseURL/protected-web-app/&gateway=true"
	}
}

