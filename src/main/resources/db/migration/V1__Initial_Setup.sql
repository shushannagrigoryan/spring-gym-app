CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    username   VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    is_active  BOOLEAN      NOT NULL
);

CREATE TABLE trainees
(
    id            SERIAL PRIMARY KEY,
    date_of_birth DATE,
    address       VARCHAR(255),
    user_id       BIGINT REFERENCES users (id)
);

CREATE TABLE trainers
(
    id                SERIAL PRIMARY KEY,
    specialization_id BIGINT NOT NULL,
    user_id           BIGINT REFERENCES users (id)
);

CREATE TABLE training_types
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE trainings
(
    id                SERIAL PRIMARY KEY,
    training_name     VARCHAR(255)   NOT NULL,
    training_type_id  BIGINT REFERENCES training_types (id),
    training_date     DATE,
    training_duration DECIMAL(10, 2) NOT NULL,
    trainee_id        BIGINT REFERENCES trainees (id),
    trainer_id        BIGINT REFERENCES trainers (id)
);

CREATE TABLE trainee_trainer
(
    trainee_id BIGINT REFERENCES trainees (id),
    trainer_id BIGINT REFERENCES trainers (id),
    PRIMARY KEY (trainee_id, trainer_id)
);
