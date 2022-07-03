drop table order_line_item;

CREATE TABLE order_line_item (
     id BIGINT(20) NOT NULL AUTO_INCREMENT,
     seq BIGINT(20) NOT NULL,
     order_id BIGINT(20) NOT NULL,
     menu_id BIGINT(20) NOT NULL,
     quantity BIGINT(20) NOT NULL,
     PRIMARY KEY (id)
);

ALTER TABLE order_line_item
    ADD CONSTRAINT fk_order_line_item_orders
        FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE order_line_item
    ADD CONSTRAINT fk_order_line_item_menu
        FOREIGN KEY (menu_id) REFERENCES menu (id);