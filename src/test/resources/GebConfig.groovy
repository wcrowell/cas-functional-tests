
import geb.driver.SauceLabsDriverFactory

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver

def final String CHROME_DRIVER = "src/test/resources/chromedriver"
def final String IE_DRIVER = "src/test/resources/IEDriverServer.exe"
def final String CAS_TEST_PROPERTIES = "src/test/resources/cas-test.properties"

Properties testProperties = new Properties()

File propertiesFile = new File(CAS_TEST_PROPERTIES)
propertiesFile.withInputStream {
	is -> testProperties.load(is)
}

if (testProperties.host == null) {
	// Note: InetAddress.localHost.hostAddress must return the bind address of the application server that CAS is running on.
	testProperties.setProperty("host", InetAddress.localHost.hostAddress)
}

// The next line makes properties available to all tests and pages globally
properties = testProperties
baseUrl = "$properties.protocol://$properties.host:$properties.port"

// default driver...
System.setProperty('webdriver.chrome.driver', CHROME_DRIVER)

File cDriver = new File(CHROME_DRIVER);

// Is it executable
if (!cDriver.canExecute()) {
	cDriver.setExecutable(true);
}

driver = {
   if (properties.headless == "true") {
	   new HtmlUnitDriver()
   } else {
	   ChromeOptions options = new ChromeOptions()
	   def browserOptions = properties.getProperty("browser.options", null)
	   
	   if (browserOptions != null) {
		   browserOptions.split(" ").each {
			   options.addArguments(it)
		   }
	   }
	   new ChromeDriver(options)
   }
}

driver.javascriptEnabled = true

// Note: Only HtmlUnit and Chrome are supported at the moment.  Still working on other browsers.
environments {
    // specify environment via -Dgeb.env=ie
    "ie" {
        def ieDriver = new File(IE_DRIVER)
        System.setProperty('webdriver.ie.driver', ieDriver.absolutePath)
        driver = { new InternetExplorerDriver() }
    }

    "chrome" {
        def chromeDriver = new File(CHROME_DRIVER) // add .exe for Windows...
        System.setProperty('webdriver.chrome.driver', chromeDriver.absolutePath)
		
        driver = { new ChromeDriver() }
    }

    'ff' {
        driver = { new FirefoxDriver() }
        driver.manage().window().maximize()
    }

    'safari' {
        driver = { new SafariDriver() }
    }

    'sauce' {
        driver = {
            // sauce.config: <browser>:<os>:<ver> eg. iphone:OSX10.9:7
            def sauceBrowser = System.properties.getProperty('sauce.config')
            def username = System.properties.getProperty('sauce.user')
            def accessKey = System.properties.getProperty('sauce.key')
            new SauceLabsDriverFactory().create(sauceBrowser, username, accessKey)
        }
    }
}

waiting {
    timeout = 6
    retryInterval = 0.5
    slow { timeout = 12 }
    reallySlow { timeout = 24 }
}

reportsDir = "target/geb-reports"
