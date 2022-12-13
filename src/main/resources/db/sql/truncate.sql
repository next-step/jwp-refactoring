SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE ORDERS;
TRUNCATE TABLE ORDER_LINE_ITEM;
TRUNCATE TABLE MENU;
TRUNCATE TABLE MENU_GROUP;
TRUNCATE TABLE MENU_PRODUCT;
TRUNCATE TABLE ORDER_TABLE;
TRUNCATE TABLE TABLE_GROUP;
TRUNCATE TABLE PRODUCT;

ALTER TABLE ORDERS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE ORDER_LINE_ITEM ALTER COLUMN SEQ RESTART WITH 1;
ALTER TABLE MENU ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE MENU_GROUP ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE MENU_PRODUCT ALTER COLUMN SEQ RESTART WITH 1;
ALTER TABLE ORDER_TABLE ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE TABLE_GROUP ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE PRODUCT ALTER COLUMN ID RESTART WITH 1;

SET FOREIGN_KEY_CHECKS = 1;