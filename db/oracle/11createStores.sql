CREATE TABLE STORES (
  ID   NUMBER(6) NOT NULL UNIQUE,
  NAME VARCHAR(128),
PRIMARY KEY(ID)
);

GRANT ALL ON STORES TO PUBLIC;
