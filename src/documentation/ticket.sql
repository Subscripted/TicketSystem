CREATE TABLE IF NOT EXISTS ticket (
  id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    title              VARCHAR(255) DEFAULT '',
    type               INT          DEFAULT 0  NOT NULL,
    status             INT          DEFAULT 0  NOT NULL,
    notiz              TEXT         DEFAULT '' NOT NULL,
    assigned_user_id   BIGINT,
    assigned_tester_id BIGINT,
    tester             TINYINT(1)   DEFAULT 0,
    date_created       DATETIME                NOT NULL,
    date_last_updated  DATETIME                NOT NULL,
    CONSTRAINT assigned_user FOREIGN KEY (assigned_user_id) REFERENCES ticket_user (user_id),
    CONSTRAINT assigned_tester FOREIGN KEY (assigned_tester_id) REFERENCES ticket_user (user_id)
);
