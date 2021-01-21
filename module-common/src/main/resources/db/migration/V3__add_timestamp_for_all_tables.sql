ALTER TABLE orders
    ADD (created_date timestamp, modified_date timestamp);

ALTER TABLE order_line_item
    ADD (created_date timestamp, modified_date timestamp);

ALTER TABLE menu
    ADD (created_date timestamp, modified_date timestamp);

ALTER TABLE menu_group
    ADD (created_date timestamp, modified_date timestamp);

ALTER TABLE menu_product
    ADD (created_date timestamp, modified_date timestamp);

ALTER TABLE order_table
    ADD (created_date timestamp, modified_date timestamp);

ALTER TABLE table_group
    ADD (modified_date timestamp);

ALTER TABLE product
    ADD (created_date timestamp, modified_date timestamp);
