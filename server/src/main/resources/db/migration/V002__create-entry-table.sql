CREATE TABLE entry (
  user_id       BIGINT NOT NULL,
  message_id    BIGINT NOT NULL PRIMARY KEY,
  message_type  VARCHAR NOT NULL,
  content       VARCHAR NOT NULL,
  post_date     TIMESTAMP NOT NULL,
  create_date   TIMESTAMP NOT NULL,
  update_date   TIMESTAMP NOT NULL
);