CREATE SEQUENCE IF NOT EXISTS tb_revinfo_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE tb_user
(
    id                      UUID         NOT NULL,
    created_at              TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITHOUT TIME ZONE,
    created_by              VARCHAR(100),
    updated_by              VARCHAR(100),
    username                VARCHAR(255) NOT NULL,
    full_name               VARCHAR(255) NOT NULL,
    email                   VARCHAR(255) NOT NULL,
    password                VARCHAR(255) NOT NULL,
    account_non_expired     BOOLEAN      NOT NULL,
    account_non_locked      BOOLEAN      NOT NULL,
    credentials_non_expired BOOLEAN      NOT NULL,
    enabled                 BOOLEAN      NOT NULL,
    CONSTRAINT pk_tb_user PRIMARY KEY (id)
);

CREATE TABLE tb_user_aud
(
    rev                     INTEGER NOT NULL,
    revtype                 SMALLINT,
    id                      UUID    NOT NULL,
    username                VARCHAR(255),
    full_name               VARCHAR(255),
    email                   VARCHAR(255),
    password                VARCHAR(255),
    account_non_expired     BOOLEAN,
    account_non_locked      BOOLEAN,
    credentials_non_expired BOOLEAN,
    enabled                 BOOLEAN,
    CONSTRAINT pk_tb_user_aud PRIMARY KEY (rev, id)
);

CREATE TABLE tb_user_permission
(
    permission_id UUID NOT NULL,
    user_id       UUID NOT NULL
);

CREATE TABLE tb_user_permission_aud
(
    permission_id UUID    NOT NULL,
    rev           INTEGER NOT NULL,
    revtype       SMALLINT,
    user_id       UUID    NOT NULL,
    CONSTRAINT pk_tb_user_permission_aud PRIMARY KEY (permission_id, rev, user_id)
);

ALTER TABLE tb_user
    ADD CONSTRAINT uc_tb_user_email UNIQUE (email);

ALTER TABLE tb_user
    ADD CONSTRAINT uc_tb_user_username UNIQUE (username);

ALTER TABLE tb_user_aud
    ADD CONSTRAINT FK_TB_USER_AUD_ON_REV FOREIGN KEY (rev) REFERENCES tb_revinfo (rev);

ALTER TABLE tb_user_permission_aud
    ADD CONSTRAINT fk_tb_user_permission_aud_on_rev FOREIGN KEY (rev) REFERENCES tb_revinfo (rev);

ALTER TABLE tb_user_permission
    ADD CONSTRAINT fk_tbuseper_on_permission FOREIGN KEY (permission_id) REFERENCES tb_permission (id);

ALTER TABLE tb_user_permission
    ADD CONSTRAINT fk_tbuseper_on_user FOREIGN KEY (user_id) REFERENCES tb_user (id);