create database if not exists test_db;

use test_db;
CREATE TABLE IF NOT EXISTS account
(
    id             BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(255)   NOT NULL,
    balance        DECIMAL(20, 2) NOT NULL DEFAULT 0,
    create_time    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    update_time    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE IF NOT EXISTS transaction_records
(
    transaction_id    BIGINT UNSIGNED NOT NULL PRIMARY KEY,
    transaction_time  TIMESTAMP,
    source_account_id BIGINT UNSIGNED NOT NULL,
    target_account_id BIGINT UNSIGNED NOT NULL,
    amount            DECIMAL(20, 2)  NOT NULL,
    status            VARCHAR(20)     NOT NULL,
    create_time       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    update_time       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP()
);