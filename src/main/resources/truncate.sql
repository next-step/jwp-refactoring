set referential_integrity false
truncate table product
truncate table menu_group
truncate table menu_product
truncate table menu
truncate table orders
truncate table order_line_item
truncate table order_table
truncate table table_group
alter table product alter column id restart with 1
alter table menu_group alter column id restart with 1
alter table menu_product alter column seq restart with 1
alter table menu alter column id restart with 1
alter table orders alter column id restart with 1
alter table order_line_item alter column seq restart with 1
alter table order_table alter column id restart with 1
alter table table_group alter column id restart with 1
set referential_integrity true
