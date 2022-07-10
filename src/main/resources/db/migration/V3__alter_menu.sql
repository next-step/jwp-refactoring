ALTER TABLE order_line_item
    ADD menu_name VARCHAR(255) NOT NULL;
ALTER TABLE order_line_item
    ADD price DECIMAL(19, 2) NOT NULL;

UPDATE order_line_item order_line_item
SET order_line_item.menu_name = (SELECT name FROM menu WHERE id = order_line_item.menu_id),
    order_line_item.price     = (SELECT price * order_line_item.quantity FROM menu WHERE id = order_line_item.menu_id)
WHERE exists(SELECT * FROM menu WHERE id = order_line_item.menu_id);
