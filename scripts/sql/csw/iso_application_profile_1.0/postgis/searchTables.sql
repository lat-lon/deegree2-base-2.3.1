CREATE TABLE CQP_Main ( 
	FK_METADATA integer,	
	-- keywords/topicCategory can occure multiple times
	subject text,
	title varchar(200),
	abstract text,
	anyText text,
	identifier varchar(250),
	modified timestamp,
	type varchar(50),
	revisionDate timestamp,
	creationDate timestamp,
	publicationDate timestamp,
	-- alternateTitle can occure multiple times
	alternateTitle text,
	-- resourceIdentifier can occure multiple times
	resourceIdentifier text,
	-- resourceLanguage can occure multiple times
	resourceLanguage text,
	-- geographicDescripionCode can occure multiple times
	geographicDescripionCode text,
	serviceType varchar(50),
	topicCategory text,
	parentId varchar(150),
	ID integer DEFAULT NEXTVAL('CQP_MAIN_ID_seq'::TEXT)
);

CREATE TABLE CQP_BBOX ( 
	FK_CQP_MAIN integer,
	ID integer DEFAULT NEXTVAL('CQP_BBOX_ID_seq'::TEXT)
);

SELECT AddGeometryColumn('', 'cqp_bbox','geom',-1,'POLYGON',2);


CREATE SEQUENCE CQP_MAIN_ID_seq;
CREATE SEQUENCE CQP_BBOX_ID_seq;