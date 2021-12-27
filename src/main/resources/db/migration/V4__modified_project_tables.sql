ALTER TABLE menu_product ALTER COLUMN menu_id DROP NOT NULL;

ALTER TABLE order_table ADD order_id BIGINT(20);
ALTER TABLE order_table ADD table_status VARCHAR(255);
ALTER TABLE order_table DROP COLUMN empty;

ALTER TABLE order_line_item ALTER COLUMN order_id DROP NOT NULL;
ALTER TABLE order_line_item ADD menu_name VARCHAR(255) NOT NULL;
ALTER TABLE order_line_item ADD menu_price DECIMAL(19, 2) NOT NULL;

DELETE FROM order_table;

INSERT INTO order_table (id, number_of_guests, table_status) VALUES (1, 0, 'EMPTY');
INSERT INTO order_table (id, number_of_guests, table_status) VALUES (2, 0, 'EMPTY');
INSERT INTO order_table (id, number_of_guests, table_status) VALUES (3, 0, 'EMPTY');
INSERT INTO order_table (id, number_of_guests, table_status) VALUES (4, 0, 'EMPTY');
INSERT INTO order_table (id, number_of_guests, table_status) VALUES (5, 0, 'EMPTY');
INSERT INTO order_table (id, number_of_guests, table_status) VALUES (6, 0, 'EMPTY');
INSERT INTO order_table (id, number_of_guests, table_status) VALUES (7, 0, 'EMPTY');
INSERT INTO order_table (id, number_of_guests, table_status) VALUES (8, 0, 'EMPTY');