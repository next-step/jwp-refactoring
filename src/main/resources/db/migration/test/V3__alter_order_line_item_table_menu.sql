ALTER TABLE order_line_item
    ADD COLUMN menu_name VARCHAR(100) NOT NULL;
ALTER TABLE order_line_item
    ADD COLUMN menu_price DECIMAL(10, 0) NOT NULL;
