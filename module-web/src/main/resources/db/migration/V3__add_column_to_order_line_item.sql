ALTER TABLE order_line_item ADD COLUMN (name VARCHAR(255));
ALTER TABLE order_line_item ADD COLUMN (price DECIMAL(19, 2));

DELETE FROM order_line_item WHERE seq IN (1, 2, 3, 4);

INSERT INTO order_line_item (seq, name, price, order_id, menu_id, quantity) VALUES (1, '반반치킨', 16000, 1, 3, 1);
INSERT INTO order_line_item (seq, name, price, order_id, menu_id, quantity) VALUES (2, '후라이드치킨', 16000, 2, 1, 1);
INSERT INTO order_line_item (seq, name, price, order_id, menu_id, quantity) VALUES (3, '양념치킨', 16000, 2, 2, 1);
INSERT INTO order_line_item (seq, name, price, order_id, menu_id, quantity) VALUES (4, '순살치킨', 16000, 3, 6, 1);
