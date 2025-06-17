CREATE TABLE IF NOT EXISTS ticket_times
(
    insertId          BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    ticket_id         BIGINT NOT NULL,
    ticket_user_id    BIGINT   NOT NULL,
    time              INT      NOT NULL DEFAULT 0,
    date_insert       DATETIME NOT NULL,
    date_last_updated DATETIME NOT NULL,

    CONSTRAINT fk_ticket FOREIGN KEY (ticket_id) REFERENCES ticket (id),
    CONSTRAINT fk_ticket_user FOREIGN KEY (ticket_user_id) REFERENCES ticket_user (user_id)
);
