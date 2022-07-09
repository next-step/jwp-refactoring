ALTER TABLE order_line_item ADD menu_name VARCHAR(255) NOT NULL;
ALTER TABLE order_line_item ADD price DECIMAL(19, 2) NOT NULL;

UPDATE order_line_item oli
SET oli.menu_name = (SELECT name FROM menu WHERE id = oli.menu_id),
    oli.price = (SELECT price * oli.quantity FROM menu WHERE id = oli.menu_id)
WHERE exists (SELECT * FROM menu WHERE id = oli.menu_id);
