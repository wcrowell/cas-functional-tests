# To run: gradle clean farmIntegrationTest

# CAS Function Tests
============================

# CAS function tests for validating the cas-server-webapp and cas-management-webapp artifacts to exercise the latest versions of CAS.  

# Requirements
* JDK 1.7
* Groovy 2.4.4+
* Gradle 2.10+
* Google Chrome (For more details see: https://sites.google.com/a/chromium.org/chromedriver/getting-started)
* Note: If you are using this set of tests in a headless environment (console-based), then please change the driver from ChromeDriver to HtmlUnitDriver in lines 29 and 30 of src/test/resources/GebConfig.groovy.  

# Build

```bash
gradle clean farmIntegrationTest
```
# Deployment

## Embedded Jetty

* A Java keystore is already created in the projects `/etc/jetty/keystore` folder with the password `changeit`. 
* Import your CAS server certificate inside this keystore.

# CAS will be available during the unit test at:

* `http://localhost:8080/cas`
* `https://localhost:8443/cas`

# CAS Service Management be available during the unit test at:

* `http://localhost:8080/cas-services`
* `https://localhost:8443/cas-services`

# A sample, CAS Example Protected Web Application, is registered and deployed during the test at:

* `http://localhost:8080/protected-web-app`
* `https://localhost:8443/protected-web-app`



