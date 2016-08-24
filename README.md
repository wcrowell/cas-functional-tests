## There are 3 branches:

1. **master**: Runs an embedded Jetty container and deploys cas-server-webapp and cas-management-webapp.  Then runs tests against CAS.
2. **4.2.x_without_embedded_container**: Runs tests against CAS 4.2.x without firing up an embedded container.
3. **5.0.x**: Runs tests against CAS 5.0.x without starting up an embedded container.  

* You must setup an embedded container with cas-server-webapp, cas-management-webapp, and protected-web-app for branches 4.2.x_without_embedded_container and 5.0.x.

To run: 

* master: `gradle clean farmIntegrationTest`
* 4.2.x_without_embedded_container: `gradle clean test`
* 5.0.x: `gradle clean test`

Eventually, master will be moved to a branch called 4.2.x_embedded_container and the 5.0.x branch will be moved to master.

## CAS Functional Tests

Purpose of tests: CAS function tests for validating the cas-server-webapp and cas-management-webapp artifacts to exercise the latest versions of CAS.  

### Requirements
* JDK 1.7
* Groovy 2.4.4+
* Gradle 2.10+
* Optional: Google Chrome (For more details see: https://sites.google.com/a/chromium.org/chromedriver/getting-started).  If you do not use Chrome, then HtmlUnit can be used instead.  See the notes below for running on HtmlUnit.

### Notes:
* If you are using this set of tests in a headless environment (console-based), then please change the headless boolean from false to true so that the HtmlUnitDriver is used instead of the to ChromeDriver. 
* For an embedded container test, Self-signed certificate with common name (CN) localhost is stored in /etc/jetty/keystore.  Jetty can generate it's own keystore, but that keystore can change each time Jetty is started.   Therefore, this project ships with an SSL keystore already prepared with a valid cert suitable to run the entire test without any additional steps required.  The self-signed certificate is valid for 10 years until 2026. 
* There have been issues running these tests with some versions of Java 8 which are still being investigated.  
* Some of the tests will fail with Firefox as that browser prompts the user with some modal dialog boxes.  This is currently being investigated as well.
* If you do not trust the chromedriver that is in src/test/resources, then you are welcome to download chromedriver from https://sites.google.com/a/chromium.org/chromedriver/downloads and replace it.

## Deployment

## Embedded Jetty Notes:

* A Java keystore is already created in the projects `/etc/jetty/keystore` folder with the password `changeit`. 
* Import your CAS server certificate inside this keystore.

### CAS will be available during the unit test at:

* `http://localhost:8080/cas`
* `https://localhost:8443/cas`

### CAS Service Management be available during the unit test at:

* `http://localhost:8080/cas-services`
* `https://localhost:8443/cas-services`

### A sample, CAS Example Protected Web Application, is registered and deployed during the test at:

* `http://localhost:8080/protected-web-app`
* `https://localhost:8443/protected-web-app`

