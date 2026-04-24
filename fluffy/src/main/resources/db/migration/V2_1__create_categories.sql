CREATE TABLE category_types
(
    id                  BIGINT  PRIMARY KEY AUTO_INCREMENT,
    category_type_name  VARCHAR(50) NOT NULL    COMMENT 'カテゴリ種別名',
    sort_order          INT         NOT NULL    COMMENT '表示順',
    is_income           BOOLEAN     NOT NULL    COMMENT '収入ならtrue、支出ならfalse',

    version     INT      NOT NULL,
    created_at  DATETIME NOT NULL,
    updated_at  DATETIME NOT NULL
) COMMENT '[MASTER] カテゴリー種別'
;


CREATE TABLE categories
(
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'カテゴリID',
    user_id             CHAR(36)                COMMENT 'ユーザーID（ユーザー独自のカテゴリーの場合）',
    category_type_id    BIGINT NOT NULL         COMMENT 'カテゴリ種別ID',
    category_name       VARCHAR(50) NOT NULL    COMMENT 'カテゴリ名',
    sort_order          INT         NOT NULL    COMMENT '表示順',

    version     INT      NOT NULL,
    created_at  DATETIME NOT NULL,
    updated_at  DATETIME NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_type_id) REFERENCES category_types(id) ON DELETE CASCADE
) COMMENT '[TRANSACTION] カテゴリー'
;

CREATE TABLE category_mappings
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         CHAR(36)                COMMENT 'ユーザーID（ユーザー独自のカテゴリーの場合）',
    category_id     BIGINT                  COMMENT 'カテゴリID',
    keyword         VARCHAR(255) NOT NULL   COMMENT 'キーワード',

    version     INT      NOT NULL,
    created_at  DATETIME NOT NULL,
    updated_at  DATETIME NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
) COMMENT '[TRANSACTION] カテゴリー自動振り分けルール'
;

ALTER TABLE transactions
    ADD COLUMN category_id BIGINT NOT NULL COMMENT 'カテゴリID' AFTER description,
    ADD FOREIGN KEY (category_id) REFERENCES categories(id)
;
