ALTER TABLE order_line_item_detail ADD COLUMN (product_id BIGINT(20) NOT NULL);

ALTER TABLE order_line_item_detail
    ADD CONSTRAINT fk_order_line_item_detail_product
        FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE order_line_item_detail
    RENAME CONSTRAINT fk_order_menu_product_order_line_item TO fk_order_line_item_detail_order_line_item;
