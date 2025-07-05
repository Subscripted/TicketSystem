CREATE TABLE IF NOT EXISTS ticket_user
(
    user_id           BIGINT PRIMARY KEY,
    name              VARCHAR(50) DEFAULT '' NOT NULL,
    short_name        VARCHAR(5)  DEFAULT '' NOT NULL,
    date_created      DATETIME               NOT NULL,
    date_insert       DATETIME               NOT NULL,
    date_last_updated DATETIME               NOT NULL,

    CONSTRAINT fk_user_login
        FOREIGN KEY (user_id)
            REFERENCES Ticket_Login_Creds (id)
            ON DELETE CASCADE
);