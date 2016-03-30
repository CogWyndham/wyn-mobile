@Echo off
REM Use the command below to see a complete list of options that can be specified while running the Selenium server from the command line
REM This includes options for running Selenium as a Selenium RC server, as a RemoteWebDriver server, or as a Selenium Grid hub/node
REM java -jar "C:\Javalibs\Selenium\selenium-server-standalone-2.44.0.jar" -help

REM Refer https://code.google.com/p/selenium/wiki/Grid2 and http://www.seleniumhq.org/docs/07_selenium_grid.jsp for documentation on Selenium Grid

REM Use the command below if you want to explicitly specify the browser(s) which are included as part of this node registration
REM java -jar "C:\Javalibs\Selenium\selenium-server-standalone-2.44.0.jar" -role node -hub http://<IP-Address-Of-Grid-Hub>:<Port>/grid/register -browser "browserName=internet explorer" -browser browserName=chrome
REM Possible values for the "browserName" parameter include "internet explorer", "chrome", "firefox", "opera", "safari". These values are case sensitive!
REM You can use multiple -browser switches as shown above to register all the browsers which need to be invoked on this particular node
REM Note that specifying the browser(s) is optional. If left unspecified, selenium automatically detects all the browsers installed on this machine while registering this node

REM Use the command below to enable multiple instances of a browser to be invoked in parallel on this node
REM java -jar "C:\Javalibs\Selenium\selenium-server-standalone-2.44.0.jar" -role node -hub http://<IP-Address-Of-Grid-Hub>:<Port>/grid/register -browser browserName=chrome,maxInstances=3 -browser browserName=firefox,maxInstances=3
REM You can specify as many parallel browser instances as may be reasonable considering the processing power of this node machine
REM In general, it is recommended to be watchful while working with multiple parallel instances of Internet Explorer - there have been known issues in the past in this area

REM Use the command below to explicitly specify the version of the browser(s) which are included as part of this node registration
REM java -jar "C:\Javalibs\Selenium\selenium-server-standalone-2.44.0.jar" -role node -hub http://<IP-Address-Of-Grid-Hub>:<Port>/grid/register -browser "browserName=internet explorer",version=11
REM Note that selenium does not automatically validate the specified version number against the actual version installed on this node machine
REM Always take care to ensure that the specified version number is correct to avoid unexpected results
REM Avoid explicitly specifying the browser version unless you need to execute your tests against multiple versions of the same browser

REM Use the command below if you want to explicitly specify the platform on which the current node is registered
REM java -jar "C:\Javalibs\Selenium\selenium-server-standalone-2.44.0.jar" -role node -hub http://<IP-Address-Of-Grid-Hub>:<Port>/grid/register -browser "browserName=internet explorer",platform=WINDOWS
REM Possible values for the "platform" parameter include WINDOWS, XP, VISTA, LINUX, UNIX, MAC
REM Note that specifying the platform is optional. If left unspecified, selenium automatically detects the current platform while registering this node
REM Avoid explicitly specifying the platform unless you feel that selenium's auto-detection is not accurate enough

REM Use the command below to automatically detect the current platform and all the available installed browsers while registering the grid node
REM This command automatically registers 5 instances of each browser installed on the system (except for Internet Explorer, because of sporadic issues as mentioned above)
REM This command is recommended as it is the simplest one, with lesser chances of errors occurring
@Echo on
java -jar "C:\Javalibs\Selenium\selenium-server-standalone-2.44.0.jar" -role node -hub http://<IP-Address-Of-Grid-Hub>:<Port>/grid/register