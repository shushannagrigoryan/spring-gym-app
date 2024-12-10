ALTER TABLE users
    DROP COLUMN IF EXISTS role;

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id BIGINT      NOT NULL,
    role    VARCHAR(64) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);