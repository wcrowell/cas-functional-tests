package pages;

class RenewPage extends LoginPage {
	static at = {
		title == "CAS – Central Authentication Service"
	}

	String getPageUrl() {
		def properties = browser.config.rawConfig.properties
		def baseURL = browser.config.baseUrl
		url = "/" + properties."cas.context.root" + "/login?service=$baseURL/protected-web-app/&renew=true"
	}
}

