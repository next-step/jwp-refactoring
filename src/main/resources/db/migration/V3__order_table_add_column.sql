alter table order_table add order_id BIGINT(20);

ALTER TABLE order_table
    ADD CONSTRAINT fk_order_table_orders
        FOREIGN KEY (order_id) REFERENCES orders (id);
