drop table menu_product;
drop table order_line_item;

CREATE TABLE menu_product (
      seq BIGINT(20) NOT NULL AUTO_INCREMENT,
      menu_id BIGINT(20) NOT NULL,
      product_id BIGINT(20) NOT NULL,
      quantity BIGINT(20) NOT NULL,
      PRIMARY KEY (seq)
);

CREATE TABLE order_line_item (
         seq BIGINT(20) NOT NULL AUTO_INCREMENT,
         order_id BIGINT(20) NOT NULL,
         menu_id BIGINT(20) NOT NULL,
         quantity BIGINT(20) NOT NULL,
         PRIMARY KEY (seq)
);

ALTER TABLE menu_product
    ADD CONSTRAINT fk_menu_product_menu
        FOREIGN KEY (menu_id) REFERENCES menu (id);

ALTER TABLE menu_product
    ADD CONSTRAINT fk_menu_product_product
        FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE order_line_item
    ADD CONSTRAINT fk_order_line_item_orders
        FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE order_line_item
    ADD CONSTRAINT fk_order_line_item_menu
        FOREIGN KEY (menu_id) REFERENCES menu (id);

INSERT INTO menu_product (seq, menu_id, product_id, quantity) VALUES (1, 1, 1, 1);
INSERT INTO menu_product (seq, menu_id, product_id, quantity) VALUES (2, 2, 2, 1);
INSERT INTO menu_product (seq, menu_id, product_id, quantity) VALUES (3, 3, 3, 1);
INSERT INTO menu_product (seq, menu_id, product_id, quantity) VALUES (4, 4, 4, 1);
INSERT INTO menu_product (seq, menu_id, product_id, quantity) VALUES (5, 5, 5, 1);
INSERT INTO menu_product (seq, menu_id, product_id, quantity) VALUES (6, 6, 6, 1);


