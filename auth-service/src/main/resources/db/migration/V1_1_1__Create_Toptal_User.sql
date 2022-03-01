CREATE TABLE Toptal_User (
    id              VARCHAR(256)    NOT NULL  PRIMARY KEY,
    username        VARCHAR(256)    NOT NULL  UNIQUE,
    password_hash   VARCHAR(2048)   NOT NULL
);
