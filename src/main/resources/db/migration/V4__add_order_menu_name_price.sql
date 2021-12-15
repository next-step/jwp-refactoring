ALTER TABLE order_line_item
    ADD COLUMN name VARCHAR(255) NOT NULL;
ALTER TABLE order_line_item
    ADD COLUMN price DECIMAL(19, 2) NOT NULL;

UPDATE order_line_item oli
SET name  = (SELECT name FROM menu m WHERE m.id = oli.menu_id),
    price = (SELECT price FROM menu m WHERE m.id = oli.menu_id);

ALTER TABLE order_line_item
ALTER name VARCHAR(255) NOT NULL;

ALTER TABLE order_line_item
ALTER price DECIMAL(19, 2) NOT NULL;
