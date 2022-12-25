CREATE table order_menu (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    menu_id BIGINT(20) NOT NULL,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE order_line_item ADD COLUMN order_menu_id BIGINT(20) NOT NULL;
ALTER TABLE order_line_item DROP COLUMN menu_id;
ALTER TABLE order_line_item DROP COLUMN menu_name;
ALTER TABLE order_line_item DROP COLUMN menu_price;
