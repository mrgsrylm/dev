CREATE TYPE user_role AS ENUM ('USER', 'ADMIN', 'MERCHANT');

CREATE TABLE IF NOT EXISTS users
(
	id            BIGSERIAL PRIMARY KEY,
	name          TEXT                              NOT NULL,
	email         VARCHAR(100) UNIQUE               NOT NULL,
	password_hash BYTEA                             NOT NULL,
    phone_number  VARCHAR(20)                       NOT NULL,
    role          user_role                         NOT NULL DEFAULT 'USER',
	activated     BOOL                              NOT NULL,
    created_at    TIMESTAMP(0) WITH TIME ZONE       NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP(0) WITH TIME ZONE       NOT NULL DEFAULT NOW(),
	version       INTEGER                           NOT NULL DEFAULT 1
);