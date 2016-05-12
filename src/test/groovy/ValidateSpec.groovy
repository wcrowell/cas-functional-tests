import pages.LoginPage
import groovyx.net.http.RESTClient
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpURLClient
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

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

		// Get a ticket granting ticket		
		def respTgt = client.post( path : "/" + properties."cas.context.root" + "/v1/tickets",
			body : [ username: properties.username, password: properties.password ],
			requestContentType : "application/x-www-form-urlencoded" )

		assert respTgt.status == 201
		def url = respTgt.headers.Location
		def String[] nodes = url.split("/")
		def ticket = nodes.getAt(nodes.length - 1)
		println "ticket: $ticket"
		
		// Get a service ticket	using the ticket granting ticket
		def respSt = client.post( path : "/" + properties."cas.context.root" + "/v1/tickets/$ticket",
			body : [ service: "$baseUrl/protected-web-app/" ],
			requestContentType : "application/x-www-form-urlencoded" )

		assert respSt.status == 200
		def serviceTicket = respSt.getData()

		// Validate the service ticket
		go "/" + properties."cas.context.root" + "/validate?service=$baseUrl/protected-web-app/&ticket=$serviceTicket"
		
        assert $().text() == "yes\ncasuser"
    }
}
