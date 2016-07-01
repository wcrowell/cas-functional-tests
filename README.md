# Recent change take note: Scripts were moved to org.jasig.cas.test packages in this project instead of using default package.

# To run: gradle clean farmIntegrationTest

# CAS Function Tests
============================

# CAS function tests for validating the cas-server-webapp and cas-management-webapp artifacts to exercise the latest versions of CAS.  

# Requirements
* JDK 1.7
* Groovy 2.4.4+
* Gradle 2.10+
* Optional: Google Chrome (For more details see: https://sites.google.com/a/chromium.org/chromedriver/getting-started).  If you do not use Chrome, then HtmlUnit can be used instead.  See the notes below for running on HtmlUnit.

* Notes:
* If you are using this set of tests in a headless environment (console-based), then please change the headless boolean from false to true so that the HtmlUnitDriver is used instead of the to ChromeDriver. 
* Self-signed certificate with common name (CN) localhost is stored in /etc/jetty/keystore.  Jetty can generate it's own keystore, but that keystore can change each time Jetty is started.   Therefore, this project ships with an SSL keystore already prepared with a valid cert suitable to run the entire test without any additional steps required.  The self-signed certificate is valid for 10 years until 2026. 
* There have been issues running these tests with some versions of Java 8 which are still being investigated.  
* Some of the tests will fail with Firefox as that browser prompts the user with some modal dialog boxes.  This is currently being investigated as well.
* If you do not trust the chromedriver that is in src/test/resources, then you are welcome to download chromedriver from https://sites.google.com/a/chromium.org/chromedriver/downloads and replace it.

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



