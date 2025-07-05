CREATE TABLE IF NOT EXISTS Ticket_Login_Creds
(
    id                BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    email             VARCHAR(255) UNIQUE NOT NULL,
    password_hash     VARCHAR(255)        NOT NULL,
    date_insert       DATETIME            NOT NULL,
    date_last_updated DATETIME            NOT NULL
);