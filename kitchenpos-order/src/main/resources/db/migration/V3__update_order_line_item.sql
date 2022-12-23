ALTER TABLE order_line_item ADD COLUMN `name` VARCHAR(255) NOT NULL AFTER `menu_id`;
ALTER TABLE order_line_item ADD COLUMN `price` DECIMAL(19, 2) NOT NULL AFTER `name`;
