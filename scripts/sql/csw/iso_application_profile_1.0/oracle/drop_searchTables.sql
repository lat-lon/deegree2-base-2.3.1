DROP SEQUENCE CQP_PublicationDate_ID_seq;
DROP SEQUENCE CQP_MAIN_ID_seq;
DROP SEQUENCE CQP_BBOX_ID_seq;
DROP TABLE CQP_PublicationDate  CASCADE CONSTRAINT PURGE;
DROP TABLE CQP_Main  CASCADE CONSTRAINT PURGE;
DROP TABLE CQP_BBOX  CASCADE CONSTRAINT PURGE;
commit;