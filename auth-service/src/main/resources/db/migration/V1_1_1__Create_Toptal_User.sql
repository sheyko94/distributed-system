CREATE TABLE Toptal_User (
    id              NVARCHAR(256)    NOT NULL  PRIMARY KEY,
    username        NVARCHAR(256)    NOT NULL  UNIQUE,
    password_hash   NVARCHAR(2048)   NOT NULL
);
