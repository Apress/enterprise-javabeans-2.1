CREATE TABLE ACCOUNT(
  ACNO VARCHAR(64) NOT NULL UNIQUE,
  ACDESC VARCHAR(128),
  ACBAL FLOAT8 NOT NULL
);
GRANT ALL ON ACCOUNT TO PUBLIC;
