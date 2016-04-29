package pages;

class ProtectedWebAppPage extends LoginPage {
	static at = {
		title == "CAS – Central Authentication Service"
	}

	String getPageUrl() {
		def properties = browser.config.rawConfig.properties
		def baseURL = getBrowser().getConfig().getBaseUrl()
		url = "/" + properties."cas.context.root" + "/login?service=$baseURL/protected-web-app/"
	}
}

