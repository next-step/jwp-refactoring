ALTER TABLE orders DROP CONSTRAINT fk_orders_order_table;

ALTER TABLE order_line_item DROP CONSTRAINT fk_order_line_item_orders;

ALTER TABLE order_line_item DROP CONSTRAINT fk_order_line_item_menu;

ALTER TABLE menu DROP CONSTRAINT fk_menu_menu_group;

ALTER TABLE menu_product DROP CONSTRAINT fk_menu_product_menu;

ALTER TABLE menu_product DROP CONSTRAINT fk_menu_product_product;

ALTER TABLE order_table DROP CONSTRAINT fk_order_table_table_group;
