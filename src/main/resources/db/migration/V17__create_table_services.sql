CREATE TABLE tb_services
(
    id                     UUID          NOT NULL,
    code                   VARCHAR(255)  NOT NULL,
    created_at             TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at             TIMESTAMP WITHOUT TIME ZONE,
    created_by             VARCHAR(100),
    updated_by             VARCHAR(100),
    name                   VARCHAR(255)  NOT NULL,
    category               VARCHAR(255)  NOT NULL,
    description            TEXT,
    default_price          NUMERIC(19,2) NOT NULL,
    estimated_time_minutes INTEGER       NOT NULL,
    enabled                BOOLEAN       NOT NULL,
    CONSTRAINT pk_tb_services PRIMARY KEY (id)
);

CREATE TABLE tb_services_aud
(
    rev                    INTEGER       NOT NULL,
    revtype                SMALLINT,
    id                     UUID          NOT NULL,
    name                   VARCHAR(255),
    category               VARCHAR(255),
    description            TEXT,
    default_price          NUMERIC(19,2),
    estimated_time_minutes INTEGER,
    enabled                BOOLEAN,
    CONSTRAINT pk_tb_services_aud PRIMARY KEY (rev, id)
);

ALTER TABLE tb_services
    ADD CONSTRAINT uc_tb_services_code UNIQUE (code);

ALTER TABLE tb_services_aud
    ADD CONSTRAINT fk_tb_services_aud_on_rev FOREIGN KEY (rev) REFERENCES tb_revinfo (rev);
