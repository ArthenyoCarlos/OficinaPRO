CREATE TABLE tb_product
(
    id             UUID          NOT NULL,
    code           VARCHAR(255)  NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at     TIMESTAMP WITHOUT TIME ZONE,
    created_by     VARCHAR(100),
    updated_by     VARCHAR(100),
    name           VARCHAR(255)  NOT NULL,
    sku            VARCHAR(255)  NOT NULL,
    category_id    UUID          NOT NULL,
    unit           VARCHAR(255)  NOT NULL,
    cost_price     NUMERIC(19,2) NOT NULL,
    sale_price     NUMERIC(19,2) NOT NULL,
    current_stock  INTEGER       NOT NULL,
    minimum_stock  INTEGER       NOT NULL,
    supplier_id    UUID,
    enabled        BOOLEAN       NOT NULL,
    CONSTRAINT pk_tb_product PRIMARY KEY (id)
);

CREATE TABLE tb_product_aud
(
    rev            INTEGER       NOT NULL,
    revtype        SMALLINT,
    id             UUID          NOT NULL,
    name           VARCHAR(255),
    sku            VARCHAR(255),
    category_id    UUID,
    unit           VARCHAR(255),
    cost_price     NUMERIC(19,2),
    sale_price     NUMERIC(19,2),
    current_stock  INTEGER,
    minimum_stock  INTEGER,
    supplier_id    UUID,
    enabled        BOOLEAN,
    CONSTRAINT pk_tb_product_aud PRIMARY KEY (rev, id)
);

ALTER TABLE tb_product
    ADD CONSTRAINT uc_tb_product_code UNIQUE (code);

ALTER TABLE tb_product
    ADD CONSTRAINT uc_tb_product_sku UNIQUE (sku);

ALTER TABLE tb_product
    ADD CONSTRAINT fk_tb_product_on_category FOREIGN KEY (category_id) REFERENCES tb_product_category (id);

ALTER TABLE tb_product
    ADD CONSTRAINT fk_tb_product_on_supplier FOREIGN KEY (supplier_id) REFERENCES tb_supplier (id);

ALTER TABLE tb_product_aud
    ADD CONSTRAINT fk_tb_product_aud_on_rev FOREIGN KEY (rev) REFERENCES tb_revinfo (rev);
