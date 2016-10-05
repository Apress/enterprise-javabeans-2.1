Examples for the Book Enterprise JavaBeans 2.1 


Overview
========

The examples in this book were developed with the application server BEA
WebLogic 6.1 (http://www., on which the present development environment is built. As
operating system SUSE Linux 7.1 was used, and as database, PostgreSQL
(http://www.postgresql.org/).
The examples were tested with 
- Bea WebLogic 6.1 under SuSE Linux 7.1 with PostgreSQL 
- Bea WebLogic 6.1 under SuSE Linux 7.3 with PostgreSQL 
- Bea WebLogic 6.1 under SuSE Linux 7.3 with Oracle 8i 
- Bea WebLogic 6.1 under Windows 2000 with Oracle 8i 
We recommend the use of the WebLogic Server from BEA in version 6.1.
The samples should work with WebLogic versions 7.0 and 8.1 as well.
If you use a application server different from WebLogic, then you should make
sure that it supports EJB 2.1 (or at least 2.0). Furthermore, certain
modifications must be made, which will be referred to in the course of this document.
All examples in this book have been embedded in a development environment to
automate and facilitate the installation and testing of the examples. Moreover,
the development environment should make it easier to experiment with the examples.
The development environment is oriented toward BEA WebLogic 6.1. Please understand
that we were unable to make the development environment available for another
application servers. However, the work required to adapt the development environment
to another application server should not be too great. 

Development Environment
=======================

Directory Structure
-------------------

build.xml
 Build Script (see also Build Tool)
 
readme.html
 this file
 
db
 contains database descriptions
 
db/postgres
 contains SQL scripts for generating the required tables in a PostgreSQL database
 
db/oracle
 contains SQL scripts for generating the required tables in an Oracle database
 
java
 contains the Java source code for the examples
 
java/db
 contains source code for the database utilities
 
java/ejb
 contains source code for Enterprise Beans
 
java/jms
 contains source code for JMS examples
 
lib
 contains libraries of third parties and later the ejb-jar files
 
tests
 contains a property file (test.properties) for the tests and the generated test logs
 

Build Tool
===========
As a build tool Ant was used. Ant is a tool written in Java and is freely available.
It runs on all operating systems on which a Java virtual machine is available.
Ant is deployed in many Java projects and has become very popular. 
The Ant build script is in the file build.xml in the root directory of the examples.
Further details on Ant can be found at this Address: http://ant.apache.org/index.html

Installing the Examples
===================

If you wish to use the development environment to automate installation and
testing of the examples, you must do the following: 

* Download and install a Java virtual machine, such as those from SUN and IBM. 
  (http://java.sun.com/j2se/1.4.1/index.html)
  (http://www-106.ibm.com/developerworks/java/?loc=dwmain)
* Download and install Ant (see the documentation that comes with ant
  for installation instructions).
  (http://ant.apache.org/index.html)
* Download log4j.
  An example uses the library log4j for logging output (Chapter 9 in the book).
  For this you must download log4j. Unpack the downloaded file, search for the
  file log4j.jar, and copy this file into the lib directory of the development
  environment. 
  (http://jakarta.apache.org/log4j/docs/index.html)
* Install the database.
  If you use Linux as operating system and Bea WebLogic as application server,
  we recommend PostgreSQL as database. It is a part of the distribution and
  freely available, that is, without charge.
  Check the documentation of the application server to see which databases it
  supports. The Oracle database is supported by all application servers on
  account of its large market share. 
  (http://www.postgresql.org)
* Install the Application server.
  Since the examples were developed with BEA WebLogic 6.1, we recommend its use.
  Version 7.0 and 8.1 will also do.
  A test license can be downloaded from http://www.bea.com/products.
  Carry out the installation according to the provider's instructions.

  The following resources must be placed in the application server: 

  - A JMS queue with the JNDI name 'javax.jms.LoggerQueue'.
    This queue is needed for the example of Chapter 6 (LoggerBean). 
  - A JMS topic with the JNDI name 'javax.jms.EJBEvents'.
    This topic is needed for the event mechanism of Chapter 9
    (package ejb.event.*). 
  - With Bea WebLogic a QueueConnectionFactory and a TopicConnectionFactory
    in JNDI are available under the respective names 'javax.jms.QueueConnectionFactory'
    and 'javax.jms.TopicConnectionFactory', which are also used in the examples.
    If you use another application server, make sure that these resources are available
    under the names. 
  - A JDBC data source with the JNDI name 'postgres'. The JNDI name has nothing todo
    with the database used. In the deployment descriptors of the examples the name
    postgres is used as a reference to a data source. 
  - The data source uses a pool of database connections (connection pool), which also
    must be set up. In the configuration for the connection pool, specify to what
    database a connection is to be made. The beans access the database connection
    over the data source. 
    Tip: If an application server explicitly supports a particular database, it
         generally provides the necessary JDBC driver. In any case, make sure
         that the application server has access to the database being used.
         If the driver for the database is not in the scope of the application server,
         you must link the library of the database provider that contains the JDBC
         driver to the CLASSPATH of the application server.
         With PostgreSQL under SuSE Linux, for example, the library is found in the
         directory /usr/share/pgsql or /usr/lib/pgsql, depending on the implementation,
         and is called jdbc*.jar. With Bea WebLogic, for example, you can link this library
         to the CLASSPATH by editing the start script (startWebLogic.sh) and appending
         the name of the library including the path to the variable CLASSPATH. 
* Modify the build script in the file build.xml. The build script must be adapted to
  the individual circumstances. The file build.xml contains extensive commentary,
  including that about planned changes. 
* Compilie the examples. Change into the root directory of the examples and execute
  the following command:
  :\>ant clean (deletes and generates the directory classes) 
  :\>ant compile (compiles the sources in the directory classes) 
  Then you should obtain a new directory 'classes' that contains the compiled sources. 

Install the database tables.
=============================
The directory db contains the necessary scripts for the databases PostgreSQL and Oracle.
In the file build.xml you can specify which database is to be used through the choice of
directory. If you use another database, you can attempt to use the existing scripts.
If they do not work, create a new directory in db, copy the scripts, and modify them as
necessary. Do not forget to refer to the new directory in the file build.xml.
With the command 
:\>ant setup_db 
you can have tables and test data generated automatically. Please understand that we are
unable to prepare scripts for all possible databases. Since the scripts are relatively
primitive, adapting one to another database should not pose too great a problem.
Hint: When you first use setup_db, you will receive a few error messages, which will
      look something like this:
      db/oracle/0003dropLogs.sql: ORA-00942: Table or view unavailable 
      or
      db/postgres/0006dropInvoice.sql: ERROR: table "invoice" does not exist.
      The tables are first deleted before they are placed. At first run they cannot
      be deleted, because they do not exist. The target setup_db can also be used to
      reinitialize (empty) the database. 

EJB Deployment.
==============
You can deploy each Enterprise Bean individually (for each bean there is a target
in the file Datei build.xml) or with the command 
:\>ant deploy_allbeans 
deploy all the Enterprise Beans at once.
The command works as follows:
The deployment descriptors of the Enterprise Bean(s) and the classes of the Enterprise
Bean(s) are packed into a temporary EJB jar file. This file is passed to the EJB compiler
of the application server, which generates the missing classes and creates an
installable EJB jar file, which is placed in the directory lib. The deployment descriptors
for the Enterprise Beans are located in the relevant directories within java/ejb.
There can be found the general deployment descriptor (ejb-jar.xml) as well are the
provider-dependent deployment descriptors (weblogic*.xml). In the provider-dependent
deployment descriptors are specifications regarding JNDI mappings, mappings of Enterprise
Bean fields to database tables, and configuration specifications such as session
timeouts and pool sizes. 

If you use an application server other than WebLogic 6.1, then you must prepare the
provider-dependent deployment descriptors and link them to the targets of the file build.xml.
The WebLogic descriptors will help to clarify things. 

Install the EJB jar files in the application server.
====================================================
The EJB jar files generated in the previous step must now be installed in the application
server. Please use the documentation of the application server in which the EJB jar files
are to be installed.
It is recommended after installation to check the output to the console (the shell in
which the server was started) of the application server or the log files of the application
server for errors. 

Testing the Examples
=====================
For each Enterprise Bean in the examples (except for the store bean) there exists an automated
test (see also Chapter 9 in the book, the section on quality assurance). First start the
application server (check the documentation). The tests can be executed with the command
:\> ant runtests 
and thus check whether the installation was successful. The test protocol is generated
in the form of an HTML file in the directory tests. If a test was unsuccessful, check the
output to the console or log file of the application server for hints as to what went wrong.
Common reasons for failed tests are the configuration of JMS resources, the database connection
pool, and the JDBC data source. We are speaking from our own experience ;-).
The directory tests contains a file with the name test.properties. This file determines
which tests are run. You can limit the number of tests to be executed by commenting out
entries (using the # sign) or you can add tests that you have programmed yourself.
Important:
Before starting a test you must modify the database settings in the file test.properties.
The test cases TestExchangeSFBean and TestMigrationBean access the database directly to
check the result of the tests.

Starting the Store Client (Chapter 5, Example: Warehouse Management)
====================================================================
First start the application server in which the Enterprise JavaBeans have been installed.
Then you can start the store client with the command 
:\> ant run_storeclient 
