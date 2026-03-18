CREATE TABLE tb_client
(
    id          UUID         NOT NULL,
    code        VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    created_by  VARCHAR(100),
    updated_by  VARCHAR(100),
    name        VARCHAR(255) NOT NULL,
    cpf_cnpj    VARCHAR(255) NOT NULL,
    phone       VARCHAR(255),
    email       VARCHAR(255),
    address     VARCHAR(255),
    city        VARCHAR(255),
    state       VARCHAR(255),
    zip_code    VARCHAR(255),
    observation VARCHAR(255),
    enabled     BOOLEAN      NOT NULL,
    CONSTRAINT pk_tb_client PRIMARY KEY (id)
);

CREATE TABLE tb_client_aud
(
    rev         INTEGER NOT NULL,
    revtype     SMALLINT,
    id          UUID    NOT NULL,
    name        VARCHAR(255),
    cpf_cnpj    VARCHAR(255),
    phone       VARCHAR(255),
    email       VARCHAR(255),
    address     VARCHAR(255),
    city        VARCHAR(255),
    state       VARCHAR(255),
    zip_code    VARCHAR(255),
    observation VARCHAR(255),
    enabled     BOOLEAN,
    CONSTRAINT pk_tb_client_aud PRIMARY KEY (rev, id)
);

ALTER TABLE tb_client
    ADD CONSTRAINT uc_tb_client_code UNIQUE (code);

ALTER TABLE tb_client
    ADD CONSTRAINT uc_tb_client_cpfcnpj UNIQUE (cpf_cnpj);

ALTER TABLE tb_client
    ADD CONSTRAINT uc_tb_client_email UNIQUE (email);

ALTER TABLE tb_client_aud
    ADD CONSTRAINT FK_TB_CLIENT_AUD_ON_REV FOREIGN KEY (rev) REFERENCES tb_revinfo (rev);
