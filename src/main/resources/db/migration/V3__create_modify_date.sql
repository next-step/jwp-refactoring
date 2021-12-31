ALTER TABLE table_group ALTER COLUMN created_date RENAME TO created_date;
ALTER TABLE orders ALTER COLUMN ordered_time RENAME TO created_date;
ALTER TABLE order_line_item ADD created_date DATETIME;
ALTER TABLE menu ADD created_date DATETIME;
ALTER TABLE menu_group ADD created_date DATETIME;
ALTER TABLE menu_product ADD created_date DATETIME;
ALTER TABLE order_table ADD created_date DATETIME;
ALTER TABLE product ADD created_date DATETIME;
ALTER TABLE orders ADD modified_date DATETIME;
ALTER TABLE table_group ADD modified_date DATETIME;
ALTER TABLE order_line_item ADD modified_date DATETIME;
ALTER TABLE menu ADD modified_date DATETIME;
ALTER TABLE menu_group ADD modified_date DATETIME;
ALTER TABLE menu_product ADD modified_date DATETIME;
ALTER TABLE order_table ADD modified_date DATETIME;
ALTER TABLE product ADD modified_date DATETIME;




