CREATE SEQUENCE IF NOT EXISTS tb_revinfo_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE tb_permission
(
    id          UUID         NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    created_by  VARCHAR(100),
    updated_by  VARCHAR(100),
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    CONSTRAINT pk_tb_permission PRIMARY KEY (id)
);

CREATE TABLE tb_permission_aud
(
    rev         INTEGER NOT NULL,
    revtype     SMALLINT,
    id          UUID    NOT NULL,
    name        VARCHAR(255),
    description VARCHAR(255),
    CONSTRAINT pk_tb_permission_aud PRIMARY KEY (rev, id)
);

CREATE TABLE tb_revinfo
(
    rev      INTEGER NOT NULL,
    revtstmp BIGINT  NOT NULL,
    username VARCHAR(100),
    CONSTRAINT pk_tb_revinfo PRIMARY KEY (rev)
);

ALTER TABLE tb_permission
    ADD CONSTRAINT uc_tb_permission_name UNIQUE (name);

ALTER TABLE tb_permission_aud
    ADD CONSTRAINT FK_TB_PERMISSION_AUD_ON_REV FOREIGN KEY (rev) REFERENCES tb_revinfo (rev);