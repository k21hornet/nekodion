CREATE TABLE transaction_types
(
    transaction_type        VARCHAR(20) PRIMARY KEY COMMENT '種別',
    transaction_type_name   VARCHAR(50) NOT NULL    COMMENT '種別名',
    sort_order              INT         NOT NULL    COMMENT '表示順',

    version     INT      NOT NULL,
    created_at  DATETIME NOT NULL,
    updated_at  DATETIME NOT NULL
) COMMENT '[MASTER] 取引種別'
;

CREATE TABLE transfers
(
    id          BIGINT  PRIMARY KEY AUTO_INCREMENT,

    version     INT      NOT NULL,
    created_at  DATETIME NOT NULL,
    updated_at  DATETIME NOT NULL
) COMMENT '[TRANSACTION] 振替の組を表す'
;

CREATE TABLE transactions
(
    id                  BIGINT          PRIMARY KEY AUTO_INCREMENT,
    user_id             CHAR(36)        NOT NULL,
    account_id          BIGINT          NOT NULL    COMMENT '口座ID',
    transaction_type    VARCHAR(20)     NOT NULL    COMMENT '種別',
    transaction_name    VARCHAR(50)     NOT NULL    COMMENT '収支名',
    amount              DECIMAL(15, 2)  NOT NULL    COMMENT '金額',
    transaction_date    DATE            NOT NULL    COMMENT '取引日',
    description         VARCHAR(255)                COMMENT '取引内容',
    transfer_id         BIGINT                      COMMENT '振替ID（NULLでなければ振替取引）',
    is_aggregated       BOOLEAN         NOT NULL    COMMENT '集計対象フラグ',
    is_confirmed        BOOLEAN         NOT NULL    COMMENT '確認済みフラグ',

    version     INT      NOT NULL,
    created_at  DATETIME NOT NULL,
    updated_at  DATETIME NOT NULL,

    FOREIGN KEY (user_id)          REFERENCES users(id),
    FOREIGN KEY (account_id)       REFERENCES accounts(id),
    FOREIGN KEY (transaction_type) REFERENCES transaction_types(transaction_type),
    FOREIGN KEY (transfer_id)      REFERENCES transfers(id),
    CHECK (amount > 0),
    CHECK (
        (transaction_type IN ('TRANSFER_OUT', 'TRANSFER_IN')
            AND transfer_id IS NOT NULL)
        OR
        (transaction_type IN ('INCOME', 'EXPENSE')
            AND transfer_id IS NULL)
    )
) COMMENT '[TRANSACTION] 入出金記録'
;
