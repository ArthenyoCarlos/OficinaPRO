CREATE SEQUENCE IF NOT EXISTS tb_supplier_code_seq
    START WITH 1
    INCREMENT BY 1;

DO $$
DECLARE
    next_code BIGINT;
BEGIN
    SELECT COALESCE(MAX(code::BIGINT), 0) + 1
    INTO next_code
    FROM tb_supplier
    WHERE code ~ '^[0-9]+$';

    EXECUTE format('ALTER SEQUENCE tb_supplier_code_seq RESTART WITH %s', next_code);
END $$;

CREATE OR REPLACE FUNCTION fn_set_tb_supplier_code()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.code IS NULL OR BTRIM(NEW.code) = '' THEN
        NEW.code := nextval('tb_supplier_code_seq')::VARCHAR;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_set_tb_supplier_code ON tb_supplier;

CREATE TRIGGER trg_set_tb_supplier_code
BEFORE INSERT ON tb_supplier
FOR EACH ROW
EXECUTE FUNCTION fn_set_tb_supplier_code();
