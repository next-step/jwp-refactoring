INSERT INTO menu_group (name) VALUES ('두마리메뉴');
INSERT INTO menu_group (name) VALUES ('한마리메뉴');
INSERT INTO menu_group (name) VALUES ('순살파닭두마리메뉴');
INSERT INTO menu_group (name) VALUES ('신메뉴');

INSERT INTO product (name, price) VALUES ('후라이드', 16000);
INSERT INTO product (name, price) VALUES ('양념치킨', 16000);
INSERT INTO product (name, price) VALUES ('반반치킨', 16000);
INSERT INTO product (name, price) VALUES ('통구이', 16000);
INSERT INTO product (name, price) VALUES ('간장치킨', 17000);
INSERT INTO product (name, price) VALUES ('순살치킨', 17000);

INSERT INTO menu (name, price, menu_group_id) VALUES ('후라이드치킨', 16000, 2);
INSERT INTO menu (name, price, menu_group_id) VALUES ('양념치킨', 16000, 2);
INSERT INTO menu (name, price, menu_group_id) VALUES ('반반치킨', 16000, 2);
INSERT INTO menu (name, price, menu_group_id) VALUES ('통구이', 16000, 2);
INSERT INTO menu (name, price, menu_group_id) VALUES ('간장치킨', 17000, 2);
INSERT INTO menu (name, price, menu_group_id) VALUES ('순살치킨', 17000, 2);

INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (1, 1, 1);
INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (2, 2, 1);
INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (3, 3, 1);
INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (4, 4, 1);
INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (5, 5, 1);
INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (6, 6, 1);

INSERT INTO order_table (number_of_guests, empty) VALUES (0, true);
INSERT INTO order_table (number_of_guests, empty) VALUES (0, true);
INSERT INTO order_table (number_of_guests, empty) VALUES (0, true);
INSERT INTO order_table (number_of_guests, empty) VALUES (0, true);
INSERT INTO order_table (number_of_guests, empty) VALUES (0, true);
INSERT INTO order_table (number_of_guests, empty) VALUES (0, true);
INSERT INTO order_table (number_of_guests, empty) VALUES (0, true);
INSERT INTO order_table (number_of_guests, empty) VALUES (0, true);

-- tableServiceTest.changeEmpty
INSERT INTO table_group (created_date) values (now());
INSERT INTO order_table (table_group_id, number_of_guests, empty) VALUES (1, 3, false);
INSERT INTO order_table (table_group_id, number_of_guests, empty) VALUES (1, 3, false);

-- tableGroupServiceTest.create
INSERT INTO order_table (number_of_guests, empty) VALUES (3, false);
INSERT INTO order_table (table_group_id, number_of_guests, empty) VALUES (1, 3, true);

-- orderServiceTest
INSERT INTO orders(order_table_id, order_status, ordered_time) VALUES(10, 'COOKING', now());
INSERT INTO orders(order_table_id, order_status, ordered_time) VALUES(10, 'COMPLETION', now());
