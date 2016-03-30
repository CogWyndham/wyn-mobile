@ECHO OFF
REM Set the value below to point to the location of all required JAR files
SET JAVALIBS=C:\Javalibs

REM Navigate to the framework's root folder
%~d0
cd %~dp0
D:
cd "D:\Projects\My Projects\CRAFT ,CRAFTLite\CRAFT_mCRAFTintegration\CRAFT_mCRAFT"
@ECHO ON

java -cp ".;.\supportlibraries\External_Jars\*" allocator.QcTestRunner %*