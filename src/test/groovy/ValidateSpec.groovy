import pages.LoginPage
import groovyx.net.http.RESTClient

class ValidateSpec extends CommonGebSpec {
	def setup() {
		super
	}

	def "CAS 1.0 validation"() {
		given:

		def baseURL = browser.config.baseUrl
		def client = new RESTClient(browser.config.baseUrl)
		// If using SSL, then this method must be called or else a javax.net.ssl.SSLHandshakeException will result due to a self-signed certificate in /etc/jetty/keystore.
		client.ignoreSSLIssues()
		
		def resp = client.post( path : "/" + properties."cas.context.root" + "/v1/tickets",
			body : [ username: properties.username, password: properties.password ],
			requestContentType : "application/x-www-form-urlencoded" )
		
		assert resp.status == 201
		def url = resp.headers.Location
		def String[] nodes = url.split("/")
		def ticket = nodes.getAt(nodes.length - 1)
		println "ticket: $ticket"
    }
}
