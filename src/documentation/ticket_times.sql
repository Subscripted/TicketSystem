
CREATE TABLE IF NOT EXISTS ticket_times
(
    ticketId                 BIGINT PRIMARY KEY,
    ticketUserId              BIGINT NOT NULL,
    time                  INT DEFAULT 0 NOT NULL,
    date_insert       DATETIME                NOT NULL,
    date_last_updated  DATETIME                NOT NULL,
    CONSTRAINT ticketId FOREIGN KEY (id) REFERENCES ticket (id),
    CONSTRAINT ticketUserId FOREIGN KEY (assigned_user_id) REFERENCES ticket_user (user_id)
);
