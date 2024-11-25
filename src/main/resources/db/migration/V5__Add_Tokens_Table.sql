CREATE TABLE IF NOT EXISTS tokens
(
    id         SERIAL PRIMARY KEY,
    token      VARCHAR(255) NOT NULL UNIQUE,
    token_type VARCHAR(50)  NOT NULL,
    revoked    BOOLEAN      NOT NULL DEFAULT FALSE,
    user_id    BIGINT       REFERENCES users (id) ON DELETE SET NULL
);
