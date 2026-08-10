CREATE TABLE email_import_logs
(
    id          BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id     CHAR(36)     NOT NULL                   COMMENT 'ユーザーID',
    message_id  VARCHAR(255) NOT NULL                   COMMENT 'メールのMessage-ID',
    received_at DATETIME     NOT NULL                   COMMENT 'メール受信日時',

    version    INT      NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users (id),
    UNIQUE (user_id, message_id)
) COMMENT = 'カード明細メール取込みの重複防止ログ'
;
