CREATE TABLE IF NOT EXISTS ticket_user
(
    user_id    BIGINT PRIMARY KEY       NOT NULL,
    email      VARCHAR(20) DEFAULT '' NOT NULL,
    name       VARCHAR(20) DEFAULT '' NOT NULL,
    short_name VARCHAR(2)  DEFAULT '' NOT NULL
);