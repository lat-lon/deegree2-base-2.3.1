
              The deegree 2.0 OGC Web Service (OWS) implementation
              ====================================================
                   	          Read me file
                              ============

In order to install and run a deegree 2.0 OGC Web Service (OWS), 
you must do the following:

a) Download and Install a Java Development Kit

* Download a Java Development Kit (JDK) release (version 1.4 or later) from:

    http://java.sun.com/j2se/

* Install the JDK according to the instructions included with the release.

* Set an environment variable JAVA_HOME to the pathname of the directory
  into which you installed the JDK release.

b) Download and install the deegree 2.0 Binary Distribution

The filename of the downloadable file includes the
date it was created (in YYYYMMDD_HHMM format).


Getting deegree 2.0 Source Distribution 
===========================================

Get the latest source code from the CVS repository:

	cvs -d:pserver:anonymous@cvs.intevation.de:/home/deegree/jail/deegreerepository login 
	cvs -z3 -d:pserver:anonymous@cvs.intevation.de:/home/deegree/jail/deegreerepository co -P deegree

or download the latest source distribution of deegree from the project's 
download page.


Compiling deegree 2.0
=====================

In order to compile deegree 2.0 the Apache Ant tool is required. deegree 2.0
requires at least Java2 SDK 1.4. 

* Download the latest version of Apache Ant from:
	http://ant.apache.org/
    (at least version 1.6.3 is required)	

* Install Ant by following the installation instructions and set an
  environment variable ANT_HOME to the pathname of the installation
  directory.

* Furthermore the optional tasks of Ant-Contrib are required. 
  Download the latest version (tested with 1.0b2) from:
  	http://ant-contrib.sourceforge.net
  and place the ant-contrib.jar in the lib directory of your Ant 
  installation.
  
* To be able to execute the 'test' target, you will need JUnit.
  Download it from:
  	http://www.junit.org
  and place the file junit.jar in the lib directory of your Ant 
  installation.

1. As long as there is no configure script apply your local setting to 
   to build.properties file. 
   Normally apply only the path to your application server and set the value 
   for 'default.server'.
   The default value is 'tomcat'. If you use TomCat apply the correct values 
   for 'tomcat.home', 'tomcat.server', 'tomcat.port', 'tomcat.username', and 
   'tomcat.password'.
   Verify settings by calling
 	>ant env

2. Get Ant projecthelp to see all targets
 	>ant -p  

3. Compile deegree source files 
	>ant compile
   and build JAR archive with
	>ant build-lib
4. To verify the build, call
   >ant test
   to run the JUnit test suite.


Running deegree 2.0 web services
================================

A) deegree 2.0 Web Feature Server (WFS 1.1.0):
----------------------------------------------
0. Set the local settings in build.properties file as described in step 
   "Compile deegree 2.0".

1. Create a web application in exploded format in a directory which is
   deployed by the application server automatically. 
   The root directory is $WEBAPP_HOME.

2. Start the application server

3. call ant build file in $DEEGREE_HOME
	>ant deploy

4. Verify if deegree 2.0 WFS services are running by opening the page 
	http://localhost:8080/deegree/
	in your web browser.


B) deegree 2.0 Catalog Service for Web (CSW 2.0.0):
---------------------------------------------
[TODO] (see WFS)

C) deegree 2.0 Web Coverage Service (WCS):
------------------------------------------
[TODO]

D) deegree 2.0 Web Map Service (WMS):
------------------------------------------
[TODO]


Migration from deegree 1.x WFS 1.0.0 to deegree 2.0 WFS 1.1.0
=============================================================
[TODO]


Supported Plattforms
====================
This version was tested on the following plattforms:
- Windows 2000/XP
	* Sun JDK 1.4.2_06 
		# Tomcat 4.1.31
		# Tomcat 5.0.28
		# BEA WebLogic 8.1.3.0
	* Sun JDK 1.5.0_04
	    #  Tomcat 5.5.9
- Linux/Suse 9.1
	* SUN JDK 1.4.2
		# Tomcat 4.1.31
		
		
Known Issues and Bugs
=====================
* The CSW example configuration requires Apache Xalan
  (http://xml.apache.org/xalan-j/) as XSLT processor. Sun's JDKs 1.4 + 1.5
  ship with Xalan bundled, so they should work out of the box.
  If you encounter XSLT related problems, check that you do not reference another
  Transformer implementation (Saxon for example) in your classpath.
