@Echo off
REM Use the command below to see a complete list of options that can be specified while running the Selenium server from the command line
REM This includes options for running Selenium as a Selenium RC server, as a RemoteWebDriver server, or as a Selenium Grid hub/node
REM java -jar "C:\Javalibs\Selenium\selenium-server-standalone-2.44.0.jar" -help

REM Refer https://code.google.com/p/selenium/wiki/Grid2 and http://www.seleniumhq.org/docs/07_selenium_grid.jsp for documentation on Selenium Grid

REM Use the command below if you want to explicitly specify the port on which you want to hub to run
REM java -jar "C:\Javalibs\Selenium\selenium-server-standalone-2.44.0.jar" -role hub -port 8888
REM Note that the -port parameter is optional. If left unspecified, the hub defaults to port 4444

REM You can view the hub status by navigating to "http://<IP-Address-Of-This-Machine>:<Port>/grid/console" in your browser
REM You can register nodes to this hub by using the URL "http://<IP-Address-Of-This-Machine>:<Port>/grid/register"

@Echo on
java -jar "C:\Javalibs\Selenium\selenium-server-standalone-2.44.0.jar" -role hub