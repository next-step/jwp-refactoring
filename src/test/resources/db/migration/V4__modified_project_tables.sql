ALTER TABLE menu_product ALTER COLUMN menu_id DROP NOT NULL;

ALTER TABLE order_table ADD order_id BIGINT(20);
ALTER TABLE order_table ADD table_status VARCHAR(255);
ALTER TABLE order_table DROP COLUMN empty;

ALTER TABLE order_line_item ALTER COLUMN order_id DROP NOT NULL;
ALTER TABLE order_line_item ADD menu_name VARCHAR(255) NOT NULL;
ALTER TABLE order_line_item ADD menu_price DECIMAL(19, 2) NOT NULL;
