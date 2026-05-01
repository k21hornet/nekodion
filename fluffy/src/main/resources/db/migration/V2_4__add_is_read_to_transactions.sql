ALTER TABLE transactions
    ADD COLUMN is_read BOOLEAN NOT NULL DEFAULT true AFTER is_confirmed;
