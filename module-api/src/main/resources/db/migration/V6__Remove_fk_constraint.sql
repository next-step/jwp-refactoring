ALTER TABLE menu_product
    DROP CONSTRAINT fk_menu_product_product;

ALTER TABLE order_line_item
    DROP CONSTRAINT fk_order_line_item_menu;

ALTER TABLE orders
    DROP CONSTRAINT fk_orders_order_table;

ALTER TABLE menu_product
    DROP CONSTRAINT fk_menu_product_menu;

ALTER TABLE order_line_item
    DROP CONSTRAINT fk_order_line_item_orders;
