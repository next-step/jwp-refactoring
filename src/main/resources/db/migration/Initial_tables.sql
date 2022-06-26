SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE orders;
ALTER TABLE orders ALTER COLUMN ID RESTART WITH 1;
TRUNCATE TABLE order_line_item;
ALTER TABLE order_line_item ALTER COLUMN ID RESTART WITH 1;
TRUNCATE TABLE menu;
ALTER TABLE menu ALTER COLUMN ID RESTART WITH 1;
TRUNCATE TABLE menu_group;
ALTER TABLE menu_group ALTER COLUMN ID RESTART WITH 1;
TRUNCATE TABLE menu_product;
ALTER TABLE menu_product ALTER COLUMN ID RESTART WITH 1;
TRUNCATE TABLE order_table;
ALTER TABLE order_table ALTER COLUMN ID RESTART WITH 1;
TRUNCATE TABLE table_group;
ALTER TABLE table_group ALTER COLUMN ID RESTART WITH 1;
TRUNCATE TABLE product;
ALTER TABLE product ALTER COLUMN ID RESTART WITH 1;

SET REFERENTIAL_INTEGRITY TRUE;
