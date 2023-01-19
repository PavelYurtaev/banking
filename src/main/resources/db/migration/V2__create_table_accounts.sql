CREATE TABLE account
(
    id      BIGINT PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    balance DECIMAL,
    CONSTRAINT fk_account__user_id FOREIGN KEY (user_id) REFERENCES users (id)
);