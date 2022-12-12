alter table menu_product add constraint fk_menu_product_to_menu foreign key (menu_id) references menu;
alter table menu_product add constraint fk_menu_product_to_product foreign key (product_id) references product;
alter table order_line_item add constraint fk_order_line_item_to_order foreign key (order_id) references orders;
alter table order_table add constraint order_table_to_table_group foreign key (table_group_id) references table_group;
