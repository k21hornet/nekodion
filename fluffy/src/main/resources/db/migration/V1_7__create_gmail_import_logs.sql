CREATE TABLE gmail_import_logs
(
    id               BIGINT         PRIMARY KEY AUTO_INCREMENT  COMMENT 'ID',
    user_id          CHAR(36)       NOT NULL                    COMMENT 'ユーザーID',
    account_id       BIGINT         NOT NULL                    COMMENT '口座ID',
    gmail_message_id VARCHAR(255)   NOT NULL                    COMMENT 'Gmail メッセージID',
    transaction_at   DATETIME       NOT NULL                    COMMENT '利用日時',
    amount           DECIMAL(15, 2) NOT NULL                    COMMENT '利用金額',
    shop_name        VARCHAR(255)                               COMMENT '利用先',

    version     INT      NOT NULL,
    created_at  DATETIME NOT NULL,
    updated_at  DATETIME NOT NULL,

    FOREIGN KEY (user_id)    REFERENCES users(id),
    FOREIGN KEY (account_id) REFERENCES accounts(id),
    UNIQUE (user_id, gmail_message_id)
) COMMENT 'Gmailインポートログ'
;
