CREATE TABLE order_status
(
    id             BIGINT(20) NOT NULL AUTO_INCREMENT,
    order_id       BIGINT(20) NOT NULL,
    order_table_id BIGINT(20) NOT NULL,
    order_status   VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE order_status
    ADD CONSTRAINT fk_order_status_orders
        FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE order_status
    ADD CONSTRAINT fk_order_status_order_table
        FOREIGN KEY (order_table_id) REFERENCES order_table (id);
