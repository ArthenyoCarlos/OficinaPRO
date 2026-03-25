CREATE TABLE tb_product_category
(
    id          UUID         NOT NULL,
    code        VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    created_by  VARCHAR(100),
    updated_by  VARCHAR(100),
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    enabled     BOOLEAN      NOT NULL,
    CONSTRAINT pk_tb_product_category PRIMARY KEY (id)
);

CREATE TABLE tb_product_category_aud
(
    rev         INTEGER NOT NULL,
    revtype     SMALLINT,
    id          UUID    NOT NULL,
    name        VARCHAR(255),
    description TEXT,
    enabled     BOOLEAN,
    CONSTRAINT pk_tb_product_category_aud PRIMARY KEY (rev, id)
);

ALTER TABLE tb_product_category
    ADD CONSTRAINT uc_tb_product_category_name UNIQUE (name);

ALTER TABLE tb_product_category
    ADD CONSTRAINT uc_tb_product_category_code UNIQUE (code);

ALTER TABLE tb_product_category_aud
    ADD CONSTRAINT fk_tb_product_category_aud_on_rev FOREIGN KEY (rev) REFERENCES tb_revinfo (rev);
