ALTER TABLE order_table DROP COLUMN empty;

ALTER TABLE orders DROP COLUMN ordered_time;
ALTER TABLE orders ADD COLUMN created_time DATETIME NOT NULL;

DROP TABLE order_line_item;

CREATE TABLE order_menu (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    order_id BIGINT(20) NOT NULL,
    menu_id BIGINT(20) NOT NULL,
    quantity BIGINT(20) NOT NULL,
    PRIMARY KEY (id)
);