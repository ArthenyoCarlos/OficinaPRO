ALTER TABLE tb_vehicle
    ADD COLUMN client_id UUID;

ALTER TABLE tb_vehicle_aud
    ADD COLUMN client_id UUID;

ALTER TABLE tb_vehicle
    ADD CONSTRAINT fk_tb_vehicle_on_client
        FOREIGN KEY (client_id) REFERENCES tb_client (id);
