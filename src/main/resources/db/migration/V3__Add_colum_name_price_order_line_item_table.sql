ALTER TABLE order_line_item ADD menu_name VARCHAR(255) AFTER menu_id;
ALTER TABLE order_line_item ADD menu_price DECIMAL(19, 2) AFTER menu_name;
