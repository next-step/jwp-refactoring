-- seq id로변경
ALTER TABLE order_line_item RENAME COLUMN seq TO id;

-- 빈테이블 여부 크기 변경 및 이름 변경
ALTER TABLE order_table ALTER COLUMN empty varchar(15);
ALTER TABLE order_table RENAME COLUMN empty TO order_table_status;

-- 데이터 형식 변경으로 값 변경
update order_table set order_table_status = 'EMPTY' where id ='1';
update order_table set order_table_status = 'EMPTY' where id ='2';
update order_table set order_table_status = 'EMPTY' where id ='3';
update order_table set order_table_status = 'EMPTY' where id ='4';
update order_table set order_table_status = 'EMPTY' where id ='5';
update order_table set order_table_status = 'EMPTY' where id ='6';
update order_table set order_table_status = 'EMPTY' where id ='7';
update order_table set order_table_status = 'EMPTY' where id ='8';

-- 테이블 그룹 생성
INSERT INTO table_group(id, created_date) values (1, now());

INSERT INTO order_table (id, table_group_id, number_of_guests, order_table_status) VALUES (9, 1, 2, 'EMPTY');
INSERT INTO order_table (id, table_group_id, number_of_guests, order_table_status) VALUES (10, 1, 2,'EMPTY');

-- 주문 생성
insert into orders(id, order_table_id, order_status, ordered_time) values(1, 9, 'COOKING', now());
insert into orders(id, order_table_id, order_status, ordered_time) values(2, 10, 'COOKING', now());
insert into order_line_item(id, order_id, menu_id, quantity) values(1, 1, 1, 1);
insert into order_line_item(id, order_id, menu_id, quantity) values(2, 2, 1, 1);

