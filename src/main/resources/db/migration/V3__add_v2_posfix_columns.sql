ALTER TABLE orders ADD COLUMN order_table_id_v2 BIGINT(20) NOT NULL AFTER order_table_id;

ALTER TABLE order_line_item ADD COLUMN order_id_v2 BIGINT(20) NOT NULL AFTER order_id;

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_order_table_v2
        FOREIGN KEY (order_table_id_v2) REFERENCES order_table (id);

ALTER TABLE order_line_item
    ADD CONSTRAINT fk_order_line_item_orders_v2
        FOREIGN KEY (order_id_v2) REFERENCES orders (id);
