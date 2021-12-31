CREATE TABLE table_group_order_table
(
    table_group_id  bigint NOT NULL,
    order_table_id bigint NOT NULL
);

ALTER TABLE table_group_order_table
    ADD CONSTRAINT fk_table_group_order_table
        FOREIGN KEY (table_group_id)
            REFERENCES table_group;