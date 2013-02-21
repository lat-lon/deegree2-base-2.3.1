CREATE DATABASE deegreetest
  WITH ENCODING='UNICODE'
       OWNER=deegreetest
       TEMPLATE=template1;

CREATE USER deegreetest PASSWORD 'deegreetest'
   VALID UNTIL 'infinity';
