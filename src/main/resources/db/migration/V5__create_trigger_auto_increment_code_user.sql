CREATE SEQUENCE IF NOT EXISTS tb_user_code_seq
    START WITH 1
    INCREMENT BY 1;

DO $$
DECLARE
    next_code BIGINT;
BEGIN
    SELECT COALESCE(MAX(code::BIGINT), 0) + 1
    INTO next_code
    FROM tb_user
    WHERE code ~ '^[0-9]+$';

    EXECUTE format('ALTER SEQUENCE tb_user_code_seq RESTART WITH %s', next_code);
END $$;

CREATE OR REPLACE FUNCTION fn_set_tb_user_code()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.code IS NULL OR BTRIM(NEW.code) = '' THEN
        NEW.code := nextval('tb_user_code_seq')::VARCHAR;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_set_tb_user_code ON tb_user;

CREATE TRIGGER trg_set_tb_user_code
BEFORE INSERT ON tb_user
FOR EACH ROW
EXECUTE FUNCTION fn_set_tb_user_code();
