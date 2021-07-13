ALTER TABLE order_table RENAME COLUMN `empty` TO `table_status` ;
ALTER TABLE order_table ALTER COLUMN `table_status` SET DATA TYPE int(11);
