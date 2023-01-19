CREATE TABLE email_data
(
    id      BIGINT PRIMARY KEY,
    user_id BIGINT              NOT NULL,
    email   VARCHAR(200) UNIQUE NOT NULL,
    CONSTRAINT fk_email_data__user_id FOREIGN KEY (user_id) REFERENCES users (id)
);