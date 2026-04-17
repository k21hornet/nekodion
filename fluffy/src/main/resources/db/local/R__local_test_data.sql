-- Accounts (user 1)
INSERT INTO accounts (id, user_id, account_type, account_template_id, account_name, version, created_at, updated_at)
VALUES
    (1, 'ae099576-fe32-431e-b6cf-fbfb0ca19f29', 'BANK', 1, '三井住友銀行',  0, NOW(), NOW()),
    (2, 'ae099576-fe32-431e-b6cf-fbfb0ca19f29', 'CARD', 4, '三井住友カード', 0, NOW(), NOW()),
    (3, 'ae099576-fe32-431e-b6cf-fbfb0ca19f29', 'CASH', 8, '財布',           0, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = updated_at;

-- Transfers (for transfer transactions)
INSERT INTO transfers (id, version, created_at, updated_at)
VALUES
    (1, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = updated_at;

-- Transactions (user 1)
INSERT INTO transactions (id, user_id, account_id, transaction_type, transaction_name, amount, transaction_date, description, transfer_id, is_aggregated, is_confirmed, version, created_at, updated_at)
VALUES
    -- 収入
    (1,  'ae099576-fe32-431e-b6cf-fbfb0ca19f29', 1, 'INCOME',       '給与',           250000.00, '2026-04-25', NULL,         NULL, TRUE,  TRUE,  0, NOW(), NOW()),
    -- 支出
    (2,  'ae099576-fe32-431e-b6cf-fbfb0ca19f29', 2, 'EXPENSE',      'スーパー',         4500.00, '2026-04-01', '食料品',     NULL, TRUE,  TRUE,  0, NOW(), NOW()),
    (3,  'ae099576-fe32-431e-b6cf-fbfb0ca19f29', 2, 'EXPENSE',      'Netflix',          1980.00, '2026-04-03', '月額サブスク', NULL, TRUE, TRUE,  0, NOW(), NOW()),
    (4,  'ae099576-fe32-431e-b6cf-fbfb0ca19f29', 2, 'EXPENSE',      'コンビニ',          650.00, '2026-04-05', NULL,         NULL, TRUE,  FALSE, 0, NOW(), NOW()),
    (5,  'ae099576-fe32-431e-b6cf-fbfb0ca19f29', 2, 'EXPENSE',      '電気代',           8200.00, '2026-04-10', NULL,         NULL, TRUE,  TRUE,  0, NOW(), NOW()),
    (6,  'ae099576-fe32-431e-b6cf-fbfb0ca19f29', 2, 'EXPENSE',      'ランチ',            950.00, '2026-04-15', NULL,         NULL, TRUE,  FALSE, 0, NOW(), NOW()),
    -- 振替（銀行 → 財布）
    (7,  'ae099576-fe32-431e-b6cf-fbfb0ca19f29', 1, 'TRANSFER_OUT', '財布へ入金',       20000.00, '2026-04-02', NULL,         1,    FALSE, TRUE,  0, NOW(), NOW()),
    (8,  'ae099576-fe32-431e-b6cf-fbfb0ca19f29', 3, 'TRANSFER_IN',  '銀行から入金',     20000.00, '2026-04-02', NULL,         1,    FALSE, TRUE,  0, NOW(), NOW()),
    -- 財布からの支出
    (9,  'ae099576-fe32-431e-b6cf-fbfb0ca19f29', 3, 'EXPENSE',      '居酒屋',           3200.00, '2026-04-12', NULL,         NULL, TRUE,  TRUE,  0, NOW(), NOW()),
    (10, 'ae099576-fe32-431e-b6cf-fbfb0ca19f29', 3, 'EXPENSE',      '自販機',            180.00, '2026-04-14', NULL,         NULL, TRUE,  TRUE,  0, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = updated_at;
