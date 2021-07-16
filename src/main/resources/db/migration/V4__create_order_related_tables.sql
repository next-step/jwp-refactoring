CREATE TABLE order_line_item_detail
(
    seq                 BIGINT(20)     NOT NULL AUTO_INCREMENT,
    order_line_item_seq BIGINT(20)     NULL,
    name                VARCHAR(255)   NOT NULL,
    price               DECIMAL(19, 2) NOT NULL,
    quantity            BIGINT(20)     NOT NULL,
    PRIMARY KEY (seq)
);

ALTER TABLE order_line_item_detail
    ADD CONSTRAINT fk_order_menu_product_order_line_item
        FOREIGN KEY (order_line_item_seq) REFERENCES order_line_item (seq);

ALTER TABLE order_line_item
    ALTER COLUMN order_id SET NULL;
