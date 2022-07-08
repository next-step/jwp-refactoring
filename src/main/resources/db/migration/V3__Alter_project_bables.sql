ALTER TABLE order_line_item ADD menu_name varchar(255) not null AFTER menu_id;
ALTER TABLE order_line_item ADD menu_price DECIMAL(19, 2) not null AFTER menu_name;
