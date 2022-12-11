ALTER TABLE orders ADD COLUMN order_table_id_v2 BIGINT(20) NOT NULL AFTER order_table_id;

ALTER TABLE order_line_item ADD COLUMN order_id_v2 BIGINT(20) NOT NULL AFTER order_id;

ALTER TABLE menu ADD COLUMN menu_group_id_v2 BIGINT(20) NOT NULL AFTER menu_id;

ALTER TABLE menu_product ADD COLUMN menu_id_v2 BIGINT(20) NOT NULL AFTER menu_id;

ALTER TABLE menu_product ADD COLUMN product_id_v2 BIGINT(20) NOT NULL AFTER product_id;

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_order_table_v2
        FOREIGN KEY (order_table_id_v2) REFERENCES order_table (id);

ALTER TABLE order_line_item
    ADD CONSTRAINT fk_order_line_item_orders_v2
        FOREIGN KEY (order_id_v2) REFERENCES orders (id);

ALTER TABLE menu
    ADD CONSTRAINT fk_menu_menu_group_v2
        FOREIGN KEY (menu_group_id_v2) REFERENCES menu_group (id);

ALTER TABLE menu_product
    ADD CONSTRAINT fk_menu_product_menu_v2
        FOREIGN KEY (menu_id_v2) REFERENCES menu (id);

ALTER TABLE menu_product
    ADD CONSTRAINT fk_menu_product_product
        FOREIGN KEY (product_id_v2) REFERENCES product (id);
