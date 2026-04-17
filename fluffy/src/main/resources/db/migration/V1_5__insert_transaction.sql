-- 収支種別
INSERT INTO transaction_types (transaction_type, transaction_type_name, sort_order, version, created_at, updated_at)
VALUES
    ('INCOME',  '収入', 1, 0, NOW(), NOW()),
    ('EXPENSE', '支出', 2, 0, NOW(), NOW()),
    ('TRANSFER_OUT','振替（出金）', 3, 0, NOW(), NOW()),
    ('TRANSFER_IN','振替（入金）', 4, 0, NOW(), NOW())
;
