ALTER TABLE users
    ADD COLUMN email_forward_token VARCHAR(64) NULL UNIQUE AFTER email;
