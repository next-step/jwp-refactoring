ALTER TABLE menu_product ALTER COLUMN menu_id DROP NOT NULL;

ALTER TABLE order_table ADD order_id BIGINT(20);
ALTER TABLE order_table ADD table_status VARCHAR(255) NOT NULL;
ALTER TABLE order_table DROP COLUMN empty;

