CREATE TABLE tb_vehicle
(
    id               UUID                        NOT NULL,
    code             VARCHAR(255)                NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    created_by       VARCHAR(100),
    updated_by       VARCHAR(100),
    plate            VARCHAR(255)                NOT NULL,
    chassis          VARCHAR(255),
    renavam          VARCHAR(255),
    brand            VARCHAR(255),
    model            VARCHAR(255),
    manufacture_year INTEGER,
    model_year       INTEGER,
    color            VARCHAR(255),
    fuel_type        VARCHAR(255),
    current_mileage  BIGINT                      NOT NULL,
    notes            VARCHAR(255),
    enabled          BOOLEAN                     NOT NULL,
    CONSTRAINT pk_tb_vehicle PRIMARY KEY (id)
);

CREATE TABLE tb_vehicle_aud
(
    rev              INTEGER NOT NULL,
    revtype          SMALLINT,
    id               UUID    NOT NULL,
    plate            VARCHAR(255),
    chassis          VARCHAR(255),
    renavam          VARCHAR(255),
    brand            VARCHAR(255),
    model            VARCHAR(255),
    manufacture_year INTEGER,
    model_year       INTEGER,
    color            VARCHAR(255),
    fuel_type        VARCHAR(255),
    current_mileage  BIGINT,
    notes            VARCHAR(255),
    enabled          BOOLEAN,
    CONSTRAINT pk_tb_vehicle_aud PRIMARY KEY (rev, id)
);

ALTER TABLE tb_vehicle
    ADD CONSTRAINT uc_tb_vehicle_code UNIQUE (code);

ALTER TABLE tb_vehicle
    ADD CONSTRAINT uc_tb_vehicle_plate UNIQUE (plate);

ALTER TABLE tb_vehicle_aud
    ADD CONSTRAINT FK_TB_VEHICLE_AUD_ON_REV FOREIGN KEY (rev) REFERENCES tb_revinfo (rev);
