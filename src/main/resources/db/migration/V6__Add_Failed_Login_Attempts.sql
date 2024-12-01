CREATE TABLE IF NOT EXISTS failed_login_attempts
(
    id                  SERIAL PRIMARY KEY,
    user_id             BIGINT REFERENCES users (id) ON DELETE SET NULL,
    failed_count        INT    NOT NULL DEFAULT 0,
    last_failed_attempt TIMESTAMP
)