-- 口座種別
INSERT INTO account_types (account_type, account_type_name, sort_order, version, created_at, updated_at)
VALUES
    ('BANK',    '銀行',             1, 0, NOW(), NOW()),
    ('CARD',    'カード',           2, 0, NOW(), NOW()),
    ('CASH',    '財布（現金管理）', 3, 0, NOW(), NOW()),
    ('OTHER',   'その他',           4, 0, NOW(), NOW())
;

-- 口座テンプレート
INSERT INTO account_templates (id, account_type, account_name, version, created_at, updated_at)
VALUES
    (1, 'BANK', '三井住友銀行',             0, NOW(), NOW()),
    (2, 'BANK', '三菱UFJ銀行',              0, NOW(), NOW()),
    (3, 'BANK', 'ゆうちょ銀行',             0, NOW(), NOW()),
    (4, 'CARD', '三井住友カード(VpassID)',  0, NOW(), NOW()),
    (5, 'CARD', 'JCBカード',                0, NOW(), NOW()),
    (6, 'CARD', 'PayPayカード',             0, NOW(), NOW()),
    (7, 'CASH', 'PayPay',                   0, NOW(), NOW()),
    (8, 'CASH', '現金',                     0, NOW(), NOW())
;
