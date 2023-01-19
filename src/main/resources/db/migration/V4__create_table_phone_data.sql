CREATE TABLE phone_data
(
    id      BIGINT PRIMARY KEY,
    user_id BIGINT             NOT NULL,
    phone   VARCHAR(13) UNIQUE NOT NULL,
    CONSTRAINT fk_phone_data__user_id FOREIGN KEY (user_id) REFERENCES users (id)
);