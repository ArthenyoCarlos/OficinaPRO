CREATE TABLE tb_service_order
(
    id                         UUID          NOT NULL,
    code                       VARCHAR(255)  NOT NULL,
    created_at                 TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at                 TIMESTAMP WITHOUT TIME ZONE,
    created_by                 VARCHAR(100),
    updated_by                 VARCHAR(100),
    number                     VARCHAR(255)  NOT NULL,
    client_id                  UUID          NOT NULL,
    vehicle_id                 UUID          NOT NULL,
    responsible_technician_id  UUID,
    opened_at                  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    expected_delivery          TIMESTAMP WITHOUT TIME ZONE,
    status                     VARCHAR(50)   NOT NULL,
    reported_problem           TEXT,
    diagnosis                  TEXT,
    internal_notes             TEXT,
    customer_notes             TEXT,
    entry_mileage              BIGINT,
    fuel_level                 VARCHAR(255),
    accessories                TEXT,
    discount                   NUMERIC(19,2),
    total_parts                NUMERIC(19,2),
    total_services             NUMERIC(19,2),
    total_amount               NUMERIC(19,2),
    financial_status           VARCHAR(50),
    approved_by                UUID,
    approval_datetime          TIMESTAMP WITHOUT TIME ZONE,
    approval_method            VARCHAR(50),
    approval_notes             TEXT,
    finalized_at               TIMESTAMP WITHOUT TIME ZONE,
    delivered_at               TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_tb_service_order PRIMARY KEY (id)
);

CREATE TABLE tb_service_order_service_item
(
    id                         UUID          NOT NULL,
    service_order_id           UUID          NOT NULL,
    service_id                 UUID          NOT NULL,
    complementary_description  TEXT,
    quantity                   INTEGER       NOT NULL,
    unit_price                 NUMERIC(19,2) NOT NULL,
    discount                   NUMERIC(19,2),
    total_amount               NUMERIC(19,2) NOT NULL,
    responsible_technician_id  UUID,
    CONSTRAINT pk_tb_service_order_service_item PRIMARY KEY (id)
);

CREATE TABLE tb_service_order_product_item
(
    id                         UUID          NOT NULL,
    service_order_id           UUID          NOT NULL,
    product_id                 UUID          NOT NULL,
    quantity                   INTEGER       NOT NULL,
    unit_price                 NUMERIC(19,2) NOT NULL,
    discount                   NUMERIC(19,2),
    total_amount               NUMERIC(19,2) NOT NULL,
    CONSTRAINT pk_tb_service_order_product_item PRIMARY KEY (id)
);

CREATE TABLE tb_service_order_status_history
(
    id                         UUID         NOT NULL,
    service_order_id           UUID         NOT NULL,
    previous_status            VARCHAR(50),
    new_status                 VARCHAR(50)  NOT NULL,
    changed_by                 UUID,
    notes                      TEXT,
    changed_at                 TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_tb_service_order_status_history PRIMARY KEY (id)
);

CREATE TABLE tb_stock_movement
(
    id                         UUID          NOT NULL,
    product_id                 UUID          NOT NULL,
    movement_type              VARCHAR(50)   NOT NULL,
    origin_type                VARCHAR(50)   NOT NULL,
    origin_id                  UUID,
    quantity                   INTEGER       NOT NULL,
    previous_balance           INTEGER       NOT NULL,
    later_balance              INTEGER       NOT NULL,
    unit_cost                  NUMERIC(19,2),
    notes                      TEXT,
    moved_by                   UUID,
    moved_at                   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_tb_stock_movement PRIMARY KEY (id)
);

CREATE TABLE tb_receipt
(
    id                         UUID          NOT NULL,
    service_order_id           UUID          NOT NULL,
    amount                     NUMERIC(19,2) NOT NULL,
    payment_method             VARCHAR(50)   NOT NULL,
    receipt_date               TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    notes                      TEXT,
    received_by                UUID,
    CONSTRAINT pk_tb_receipt PRIMARY KEY (id)
);

ALTER TABLE tb_service_order
    ADD CONSTRAINT uc_tb_service_order_code UNIQUE (code);

ALTER TABLE tb_service_order
    ADD CONSTRAINT uc_tb_service_order_number UNIQUE (number);

ALTER TABLE tb_service_order
    ADD CONSTRAINT fk_tb_service_order_on_client FOREIGN KEY (client_id) REFERENCES tb_client (id);

ALTER TABLE tb_service_order
    ADD CONSTRAINT fk_tb_service_order_on_vehicle FOREIGN KEY (vehicle_id) REFERENCES tb_vehicle (id);

ALTER TABLE tb_service_order
    ADD CONSTRAINT fk_tb_service_order_on_responsible_technician FOREIGN KEY (responsible_technician_id) REFERENCES tb_user (id);

ALTER TABLE tb_service_order
    ADD CONSTRAINT fk_tb_service_order_on_approved_by FOREIGN KEY (approved_by) REFERENCES tb_user (id);

ALTER TABLE tb_service_order_service_item
    ADD CONSTRAINT fk_tb_service_order_service_item_on_order FOREIGN KEY (service_order_id) REFERENCES tb_service_order (id);

ALTER TABLE tb_service_order_service_item
    ADD CONSTRAINT fk_tb_service_order_service_item_on_service FOREIGN KEY (service_id) REFERENCES tb_services (id);

ALTER TABLE tb_service_order_service_item
    ADD CONSTRAINT fk_tb_service_order_service_item_on_technician FOREIGN KEY (responsible_technician_id) REFERENCES tb_user (id);

ALTER TABLE tb_service_order_product_item
    ADD CONSTRAINT fk_tb_service_order_product_item_on_order FOREIGN KEY (service_order_id) REFERENCES tb_service_order (id);

ALTER TABLE tb_service_order_product_item
    ADD CONSTRAINT fk_tb_service_order_product_item_on_product FOREIGN KEY (product_id) REFERENCES tb_product (id);

ALTER TABLE tb_service_order_status_history
    ADD CONSTRAINT fk_tb_service_order_status_history_on_order FOREIGN KEY (service_order_id) REFERENCES tb_service_order (id);

ALTER TABLE tb_service_order_status_history
    ADD CONSTRAINT fk_tb_service_order_status_history_on_user FOREIGN KEY (changed_by) REFERENCES tb_user (id);

ALTER TABLE tb_stock_movement
    ADD CONSTRAINT fk_tb_stock_movement_on_product FOREIGN KEY (product_id) REFERENCES tb_product (id);

ALTER TABLE tb_stock_movement
    ADD CONSTRAINT fk_tb_stock_movement_on_user FOREIGN KEY (moved_by) REFERENCES tb_user (id);

ALTER TABLE tb_receipt
    ADD CONSTRAINT fk_tb_receipt_on_order FOREIGN KEY (service_order_id) REFERENCES tb_service_order (id);

ALTER TABLE tb_receipt
    ADD CONSTRAINT fk_tb_receipt_on_user FOREIGN KEY (received_by) REFERENCES tb_user (id);
