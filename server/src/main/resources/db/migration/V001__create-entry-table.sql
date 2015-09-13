CREATE TABLE entry (
  entry_id    BIGINT NOT NULL PRIMARY KEY,
  content       VARCHAR NOT NULL,
  post_date     TIMESTAMP NOT NULL,
  create_date   TIMESTAMP NOT NULL,
  update_date   TIMESTAMP NOT NULL
);