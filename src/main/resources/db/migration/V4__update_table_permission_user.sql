ALTER TABLE tb_permission
    ADD COLUMN code VARCHAR(255);

UPDATE tb_permission
SET code = name
WHERE code IS NULL;

ALTER TABLE tb_permission
    ALTER COLUMN code SET NOT NULL;

ALTER TABLE tb_permission
    ADD CONSTRAINT uc_tb_permission_code UNIQUE (code);

ALTER TABLE tb_permission_aud
    ADD COLUMN code VARCHAR(255);

UPDATE tb_permission_aud
SET code = name
WHERE code IS NULL;

ALTER TABLE tb_user
    ADD COLUMN code VARCHAR(255);

UPDATE tb_user
SET code = username
WHERE code IS NULL;

ALTER TABLE tb_user
    ALTER COLUMN code SET NOT NULL;

ALTER TABLE tb_user
    ADD CONSTRAINT uc_tb_user_code UNIQUE (code);

ALTER TABLE tb_user_aud
    ADD COLUMN code VARCHAR(255);

UPDATE tb_user_aud
SET code = username
WHERE code IS NULL;
