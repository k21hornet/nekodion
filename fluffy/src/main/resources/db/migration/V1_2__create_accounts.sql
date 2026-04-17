CREATE TABLE account_types
(
    account_type        VARCHAR(30) PRIMARY KEY COMMENT '口座種別',
    account_type_name   VARCHAR(50) NOT NULL    COMMENT '口座種別名',
    sort_order          INT         NOT NULL    COMMENT '表示順',

    version     INT      NOT NULL,
    created_at  DATETIME NOT NULL,
    updated_at  DATETIME NOT NULL
) COMMENT '[MASTER] 口座種別'
;

CREATE TABLE account_templates
(
    id              BIGINT          PRIMARY KEY AUTO_INCREMENT,
    account_type    VARCHAR(30)     NOT NULL        COMMENT '口座種別',
    account_name    VARCHAR(50)     NOT NULL UNIQUE COMMENT '口座名',

    version     INT      NOT NULL,
    created_at  DATETIME NOT NULL,
    updated_at  DATETIME NOT NULL,

    FOREIGN KEY (account_type) REFERENCES account_types(account_type)
) COMMENT '[MASTER] 口座テンプレート'
;

CREATE TABLE accounts
(
    id                  BIGINT          PRIMARY KEY AUTO_INCREMENT,
    user_id             CHAR(36)        NOT NULL,
    account_type        VARCHAR(30)     NOT NULL COMMENT '口座種別',
    account_template_id BIGINT                   COMMENT '口座テンプレートID',
    account_name        VARCHAR(50)     NOT NULL COMMENT '口座名',

    version     INT      NOT NULL,
    created_at  DATETIME NOT NULL,
    updated_at  DATETIME NOT NULL,

    UNIQUE(user_id, account_name),
    FOREIGN KEY (account_type) REFERENCES account_types(account_type),
    FOREIGN KEY (account_template_id) REFERENCES account_templates(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
) COMMENT '[TRANSACTION] 口座'
;
