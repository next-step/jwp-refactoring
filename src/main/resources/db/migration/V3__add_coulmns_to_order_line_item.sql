ALTER TABLE order_line_item ADD COLUMN menu_name VARCHAR (255) NOT NULL AFTER menu_id;
ALTER TABLE order_line_item ADD COLUMN menu_price DECIMAL (19, 2) NOT NULL AFTER menu_name;
