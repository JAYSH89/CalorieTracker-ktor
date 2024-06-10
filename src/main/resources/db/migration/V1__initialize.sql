CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE _user
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(60)  NOT NULL,
    created_at TIMESTAMP    NULL,
    updated_at TIMESTAMP    NULL,
    CONSTRAINT email_not_empty CHECK (email <> ''),
    CONSTRAINT password_not_empty CHECK (password <> '')
);

CREATE TABLE refresh_token
(
    token      TEXT UNIQUE,
    issued_at  TIMESTAMP,
    expires_at TIMESTAMP,
    user_id    UUID,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES _user (id) ON DELETE CASCADE,
    CONSTRAINT pk_token PRIMARY KEY (user_id)
);

CREATE TABLE weight
(
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    weight      DOUBLE PRECISION,
    measured_at TIMESTAMP NOT NULL,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    user_id     UUID REFERENCES _user (id) ON DELETE CASCADE
);

CREATE TABLE profile
(
    user_id         UUID PRIMARY KEY REFERENCES _user (id) ON DELETE CASCADE,
    first_name      VARCHAR(50),
    last_name       VARCHAR(50),
    date_of_birth   TIMESTAMP        NOT NULL,
    height          DOUBLE PRECISION NOT NULL,
    gender          VARCHAR(50)      NOT NULL,
    activity_factor VARCHAR(50)      NOT NULL
);

CREATE TABLE food
(
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(100)     NOT NULL,
    carbs       DOUBLE PRECISION NOT NULL,
    proteins    DOUBLE PRECISION NOT NULL,
    fats        DOUBLE PRECISION NOT NULL,
    amount      DOUBLE PRECISION NOT NULL,
    amount_type VARCHAR(50)      NOT NULL,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    user_id     UUID REFERENCES _user (id) ON DELETE CASCADE
);

CREATE TABLE journal
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    date       TIMESTAMP        NOT NULL,
    amount     DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    food       UUID REFERENCES food (id) ON DELETE CASCADE,
    user_id    UUID REFERENCES _user (id) ON DELETE CASCADE
);
