CREATE TABLE CQP_Main ( 
	ID number(12),
	FK_METADATA number(12),	
	-- keywords/topicCategory can occure multiple times
	subject varchar2(4000),
	title varchar2(200),
	abstract varchar2(4000),
	anytext varchar2(4000),
	identifier varchar2(250),
	modified TIMESTAMP,
	type varchar2(50),
	revisionDate TIMESTAMP,
	creationDate TIMESTAMP,
	-- alternateTitle can occure multiple times
	alternateTitle varchar2(4000),
	-- resourceIdentifier can occure multiple times
	resourceIdentifier varchar2(4000),
	-- resourceLanguage can occure multiple times
	resourceLanguage varchar2(4000),
	-- geographicDescripionCode can occure multiple times
	geographicDescripionCode varchar2(4000),
	serviceType varchar2(50),
	topicCategory varchar2(4000),
	parentId varchar2(150)	
)NOLOGGING  NOMONITORING;

CREATE TABLE CQP_PublicationDate (
	ID number (12),
	FK_CQP_MAIN number(12),
	publicationDate TIMESTAMP
)NOLOGGING  NOMONITORING;

CREATE TABLE CQP_BBOX (
	ID number(12),
	FK_CQP_MAIN number(12),
	geom sdo_geometry	
)NOLOGGING  NOMONITORING;

CREATE SEQUENCE CQP_MAIN_ID_seq increment by 1 start with 1 NOMAXVALUE minvalue 1 nocycle nocache noorder;

CREATE SEQUENCE CQP_BBOX_ID_seq increment by 1 start with 1 NOMAXVALUE minvalue 1 nocycle nocache noorder;

CREATE SEQUENCE CQP_PublicationDate_ID_seq increment by 1 start with 1 NOMAXVALUE minvalue 1 nocycle nocache noorder;

commit;