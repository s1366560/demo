create database if not exists calculation_db;

use calculation_db;

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
    transaction_time  TIMESTAMP       NOT NULL,
    transaction_type  VARCHAR(50)     NOT NULL,
    source_account_id BIGINT UNSIGNED NOT NULL,
    target_account_id BIGINT UNSIGNED NOT NULL,
    amount            DECIMAL(20, 2)  NOT NULL,
    currency          VARCHAR(10)     NOT NULL,
    status            VARCHAR(20)     NOT NULL,
    description       VARCHAR(200),
    channel           VARCHAR(50)     NOT NULL,
    transaction_fee   DECIMAL(10, 2),
    create_time       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    update_time       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP()
);