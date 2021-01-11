CREATE TABLE order_table_in_table_group (
    seq BIGINT(20) NOT NULL AUTO_INCREMENT,
    order_table_id BIGINT(20) NOT NULL,
    table_group_id BIGINT(20),
    PRIMARY KEY (seq)
);
