@Echo off
REM Use the command below to see a complete list of options that can be specified while running the Selenium server from the command line
REM This includes options for running Selenium as a Selenium RC server, as a RemoteWebDriver server, or as a Selenium Grid hub/node
REM java -jar "C:\Javalibs\Selenium\selenium-server-standalone-2.44.0.jar" -help

REM Use the command below to explicitly specify the path of the IE and Chrome Drivers by setting the corresponding system properties using the -D switch
REM java -Dwebdriver.ie.driver="C:\\Javalibs\\Selenium\\Browser Drivers\\IEDriverServer.exe" -Dwebdriver.chrome.driver="C:\\Javalibs\\Selenium\\Browser Drivers\\chromedriver.exe" -jar "C:\Javalibs\Selenium\selenium-server-standalone-2.44.0.jar"
REM Note that any other system properties can also be specified by using the -D switch if required

REM Use the command below if the location of the IE and Chrome drivers are included in the PATH environment variable
@Echo on
java -jar "C:\Javalibs\Selenium\selenium-server-standalone-2.44.0.jar"