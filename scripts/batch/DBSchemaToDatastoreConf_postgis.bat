rem example for calling 'DBSchemaToDatastoreConf' for creating a deegree 
rem WFS configuration from a shape 

C:\Programme\Java\jre1.5.0_06\bin\java -classpath .;..\..\classes;..\..\lib\postgis\postgresql-8.0-311.jdbc3.jar;..\..\lib\postgis\postgis100.jar;..\..\lib\jts\jts-1.8.jar;..\..\lib\xml\jaxen-1.1-beta-8.jar;..\..\lib\log4j\log4j-1.2.9.jar org.deegree.tools.datastore.DBSchemaToDatastoreConf -output e:/temp/schema.xsd -driver org.postgresql.Driver -url jdbc:postgresql://localhost:5432/gaz-nrw -user postgres -password postgres -tables flurst -srs EPSG:4326
pause