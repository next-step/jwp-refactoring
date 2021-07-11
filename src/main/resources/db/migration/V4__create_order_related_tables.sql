CREATE TABLE order_menu_product
(
    seq        BIGINT(20) NOT NULL AUTO_INCREMENT,
    order_line_item_seq    BIGINT(20) NOT NULL,
    quantity   BIGINT(20) NOT NULL,
    PRIMARY KEY (seq)
);

CREATE TABLE order_product
(
    id    BIGINT(20)     NOT NULL AUTO_INCREMENT,
    name  VARCHAR(255)   NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    order_menu_product_seq BIGINT(20) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE order_menu_product
    ADD CONSTRAINT fk_order_menu_product_order_line_item
        FOREIGN KEY (order_line_item_seq) REFERENCES order_line_item (seq);

ALTER TABLE order_product
    ADD CONSTRAINT fk_order_product_order_menu_product
        FOREIGN KEY (order_menu_product_seq) REFERENCES order_menu_product (seq);
