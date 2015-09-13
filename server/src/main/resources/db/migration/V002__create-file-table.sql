CREATE TABLE file(
  file_id          BIGINT NOT NULL PRIMARY KEY,
  mime_type        TEXT NOT NULL,
  length           BIGINT NOT NULL,
  create_date      TIMESTAMP NOT NULL,
  image_width      INTEGER,
  image_height     INTEGER
);
