CREATE TABLE account (
  user_id       BIGINT NOT NULL UNIQUE,
  email         VARCHAR NOT NULL PRIMARY KEY,
  create_date   TIMESTAMP NOT NULL
);