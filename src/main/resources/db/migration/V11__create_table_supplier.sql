CREATE TABLE tb_supplier
(
    id          UUID         NOT NULL,
    code        VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    created_by  VARCHAR(100),
    updated_by  VARCHAR(100),
    name        VARCHAR(255) NOT NULL,
    cnpj        VARCHAR(255) NOT NULL,
    phone       VARCHAR(255),
    email       VARCHAR(255),
    contact     VARCHAR(255),
    observation TEXT,
    enabled     BOOLEAN      NOT NULL,
    CONSTRAINT pk_tb_supplier PRIMARY KEY (id)
);

CREATE TABLE tb_supplier_aud
(
    rev         INTEGER NOT NULL,
    revtype     SMALLINT,
    id          UUID    NOT NULL,
    name        VARCHAR(255),
    cnpj        VARCHAR(255),
    phone       VARCHAR(255),
    email       VARCHAR(255),
    contact     VARCHAR(255),
    observation TEXT,
    enabled     BOOLEAN,
    CONSTRAINT pk_tb_supplier_aud PRIMARY KEY (rev, id)
);

ALTER TABLE tb_supplier
    ADD CONSTRAINT uc_tb_supplier_cnpj UNIQUE (cnpj);

ALTER TABLE tb_supplier
    ADD CONSTRAINT uc_tb_supplier_code UNIQUE (code);

ALTER TABLE tb_supplier
    ADD CONSTRAINT uc_tb_supplier_email UNIQUE (email);

ALTER TABLE tb_supplier_aud
    ADD CONSTRAINT FK_TB_SUPPLIER_AUD_ON_REV FOREIGN KEY (rev) REFERENCES tb_revinfo (rev);