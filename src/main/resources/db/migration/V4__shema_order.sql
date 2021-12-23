ALTER TABLE order_line_item RENAME COLUMN seq TO id;
ALTER TABLE order_table ALTER COLUMN empty varchar(15);
ALTER TABLE order_table RENAME COLUMN empty TO order_table_status;