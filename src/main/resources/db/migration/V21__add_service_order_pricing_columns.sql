ALTER TABLE tb_service_order
    ADD COLUMN charge_type VARCHAR(50) NOT NULL DEFAULT 'FULL',
    ADD COLUMN subtotal_parts NUMERIC(19,2),
    ADD COLUMN subtotal_services NUMERIC(19,2),
    ADD COLUMN subtotal_amount NUMERIC(19,2),
    ADD COLUMN products_discount_amount NUMERIC(19,2),
    ADD COLUMN products_discount_percent NUMERIC(10,4),
    ADD COLUMN services_discount_amount NUMERIC(19,2),
    ADD COLUMN services_discount_percent NUMERIC(10,4),
    ADD COLUMN total_discount_percent NUMERIC(10,4);

UPDATE tb_service_order
SET charge_type = 'FULL',
    subtotal_parts = COALESCE(total_parts, 0),
    subtotal_services = COALESCE(total_services, 0),
    subtotal_amount = COALESCE(total_amount, 0),
    products_discount_amount = 0,
    products_discount_percent = 0,
    services_discount_amount = 0,
    services_discount_percent = 0,
    total_discount_percent = 0
WHERE charge_type IS NULL
   OR subtotal_parts IS NULL
   OR subtotal_services IS NULL
   OR subtotal_amount IS NULL
   OR products_discount_amount IS NULL
   OR products_discount_percent IS NULL
   OR services_discount_amount IS NULL
   OR services_discount_percent IS NULL
   OR total_discount_percent IS NULL;
