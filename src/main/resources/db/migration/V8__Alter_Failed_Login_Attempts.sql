DROP TABLE IF EXISTS failed_login_attempts;

CREATE TABLE IF NOT EXISTS failed_login_attempts
(
    id                  SERIAL PRIMARY KEY,
    user_ip             VARCHAR(60) NOT NULL,
    failed_count        INT         NOT NULL DEFAULT 0,
    last_failed_attempt TIMESTAMP
)