ALTER TABLE order_line_item
    ADD COLUMN (
    name VARCHAR(255) NOT NULL,
    price DECIMAL(19, 2) NOT NULL);
