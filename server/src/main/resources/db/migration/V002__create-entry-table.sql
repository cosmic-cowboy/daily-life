CREATE TABLE entry (
  entry_id    BIGINT NOT NULL PRIMARY KEY,
  content       VARCHAR NOT NULL,
  file_id       BIGINT,
  post_date     BIGINT NOT NULL UNIQUE,
  create_date   TIMESTAMP NOT NULL,
  update_date   TIMESTAMP NOT NULL,
  CONSTRAINT fkey_entry_file_id FOREIGN KEY (file_id) REFERENCES file(file_id)
);