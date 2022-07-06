drop table menu_product;

CREATE TABLE menu_product (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    seq BIGINT(20) NOT NULL,
    menu_id BIGINT(20) NOT NULL,
    product_id BIGINT(20) NOT NULL,
    quantity BIGINT(20) NOT NULL,
    PRIMARY KEY (id)
);



ALTER TABLE menu_product
    ADD CONSTRAINT fk_menu_product_menu
        FOREIGN KEY (menu_id) REFERENCES menu (id);

ALTER TABLE menu_product
    ADD CONSTRAINT fk_menu_product_product
        FOREIGN KEY (product_id) REFERENCES product (id);


INSERT INTO menu_product (seq, menu_id, product_id, quantity) VALUES (1, 1, 1, 1);
INSERT INTO menu_product (seq, menu_id, product_id, quantity) VALUES (2, 2, 2, 1);
INSERT INTO menu_product (seq, menu_id, product_id, quantity) VALUES (3, 3, 3, 1);
INSERT INTO menu_product (seq, menu_id, product_id, quantity) VALUES (4, 4, 4, 1);
INSERT INTO menu_product (seq, menu_id, product_id, quantity) VALUES (5, 5, 5, 1);
INSERT INTO menu_product (seq, menu_id, product_id, quantity) VALUES (6, 6, 6, 1);


