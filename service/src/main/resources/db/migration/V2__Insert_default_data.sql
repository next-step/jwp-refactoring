INSERT INTO menu_group (id, name, created_at) VALUES (1, '두마리메뉴', '2021-01-21 01:23:45');
INSERT INTO menu_group (id, name, created_at) VALUES (2, '한마리메뉴', '2021-01-21 01:23:45');
INSERT INTO menu_group (id, name, created_at) VALUES (3, '순살파닭두마리메뉴', '2021-01-21 01:23:45');
INSERT INTO menu_group (id, name, created_at) VALUES (4, '신메뉴', '2021-01-21 01:23:45');

INSERT INTO product (id, name, price, created_at) VALUES (1, '후라이드', 16000, '2021-01-21 01:23:45');
INSERT INTO product (id, name, price, created_at) VALUES (2, '양념치킨', 16000, '2021-01-21 01:23:45');
INSERT INTO product (id, name, price, created_at) VALUES (3, '반반치킨', 16000, '2021-01-21 01:23:45');
INSERT INTO product (id, name, price, created_at) VALUES (4, '통구이', 16000, '2021-01-21 01:23:45');
INSERT INTO product (id, name, price, created_at) VALUES (5, '간장치킨', 17000, '2021-01-21 01:23:45');
INSERT INTO product (id, name, price, created_at) VALUES (6, '순살치킨', 17000, '2021-01-21 01:23:45');

INSERT INTO menu (id, name, price, menu_group_id, created_at) VALUES (1, '후라이드치킨', 16000, 2, '2021-01-21 01:23:45');
INSERT INTO menu (id, name, price, menu_group_id, created_at) VALUES (2, '양념치킨', 16000, 2, '2021-01-21 01:23:45');
INSERT INTO menu (id, name, price, menu_group_id, created_at) VALUES (3, '반반치킨', 16000, 2, '2021-01-21 01:23:45');
INSERT INTO menu (id, name, price, menu_group_id, created_at) VALUES (4, '통구이', 16000, 2, '2021-01-21 01:23:45');
INSERT INTO menu (id, name, price, menu_group_id, created_at) VALUES (5, '간장치킨', 17000, 2, '2021-01-21 01:23:45');
INSERT INTO menu (id, name, price, menu_group_id, created_at) VALUES (6, '순살치킨', 17000, 2, '2021-01-21 01:23:45');

INSERT INTO menu_product (menu_id, product_id, quantity, created_at) VALUES (1, 1, 1, '2021-01-21 01:23:45');
INSERT INTO menu_product (menu_id, product_id, quantity, created_at) VALUES (2, 2, 1, '2021-01-21 01:23:45');
INSERT INTO menu_product (menu_id, product_id, quantity, created_at) VALUES (3, 3, 1, '2021-01-21 01:23:45');
INSERT INTO menu_product (menu_id, product_id, quantity, created_at) VALUES (4, 4, 1, '2021-01-21 01:23:45');
INSERT INTO menu_product (menu_id, product_id, quantity, created_at) VALUES (5, 5, 1, '2021-01-21 01:23:45');
INSERT INTO menu_product (menu_id, product_id, quantity, created_at) VALUES (6, 6, 1, '2021-01-21 01:23:45');

INSERT INTO order_table (id, number_of_guests, empty, created_at) VALUES (1, 0, true, '2021-01-21 01:23:45');
INSERT INTO order_table (id, number_of_guests, empty, created_at) VALUES (2, 0, true, '2021-01-21 01:23:45');
INSERT INTO order_table (id, number_of_guests, empty, created_at) VALUES (3, 0, true, '2021-01-21 01:23:45');
INSERT INTO order_table (id, number_of_guests, empty, created_at) VALUES (4, 0, true, '2021-01-21 01:23:45');
INSERT INTO order_table (id, number_of_guests, empty, created_at) VALUES (5, 0, true, '2021-01-21 01:23:45');
INSERT INTO order_table (id, number_of_guests, empty, created_at) VALUES (6, 0, true, '2021-01-21 01:23:45');
INSERT INTO order_table (id, number_of_guests, empty, created_at) VALUES (7, 0, true, '2021-01-21 01:23:45');
INSERT INTO order_table (id, number_of_guests, empty, created_at) VALUES (8, 0, true, '2021-01-21 01:23:45');
