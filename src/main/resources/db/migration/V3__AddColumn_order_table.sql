ALTER TABLE order_table
    ADD order_status VARCHAR(255) NULL;

UPDATE order_table SET order_status = 'READY' WHERE order_status IS NULL;

ALTER TABLE order_table
    ALTER order_status VARCHAR(255) NOT NULL;