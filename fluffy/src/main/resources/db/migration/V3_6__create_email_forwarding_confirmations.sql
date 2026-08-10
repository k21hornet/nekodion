CREATE TABLE email_forwarding_confirmations
(
    user_id      CHAR(36)     PRIMARY KEY  COMMENT 'ユーザーID',
    from_address VARCHAR(255) NOT NULL     COMMENT '確認メールの差出人',
    subject      VARCHAR(255)              COMMENT '確認メールの件名',
    body_text    TEXT                      COMMENT '確認メール本文（確認コードを含む）',
    received_at  DATETIME     NOT NULL     COMMENT '確認メール受信日時',

    version    INT      NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users (id)
) COMMENT = 'メール自動転送設定の確認コード（Gmail転送確認メール等）'
;
