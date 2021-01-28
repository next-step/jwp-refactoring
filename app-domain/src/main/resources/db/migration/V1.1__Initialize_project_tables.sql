create table product (
    id bigint identity primary key,
    name varchar(255),
    price integer not null,
    created_date timestamp,
    modified_date timestamp
);

create table menu_group (
    id bigint identity primary key,
    name varchar(255)
);

create table menu (
    id bigint identity primary key,
    name varchar(255),
    price integer not null,
    menu_group_id bigint,
    created_date timestamp,
    modified_date timestamp,
    constraint MENU_FK001 foreign key (menu_group_id) references menu_group
);

create table menu_product (
    id bigint identity primary key,
    quantity integer not null,
    menu_id bigint,
    product_id bigint,
    constraint MENU_PRODUCT_FK001 foreign key (menu_id) references menu,
    constraint MENU_PRODUCT_FK002 foreign key (product_id) references product
);

create table order_table_group (
    id bigint identity primary key,
    created_date timestamp,
    modified_date timestamp
);

create table order_table (
    id bigint identity primary key,
    empty boolean not null,
    guest_number integer not null,
    order_table_group_id bigint,
    constraint ORDER_TABLE_FK001 foreign key (order_table_group_id) references order_table_group
);

create table orders (
    id bigint identity primary key,
    created_date timestamp,
    modified_date timestamp,
    order_status varchar(255),
    order_table_id bigint,
    constraint ORDERS_FK001 foreign key (order_table_id) references order_table
);

create table order_line_menu (
    id bigint identity primary key,
    quantity integer not null,
    menu_id bigint,
    order_id bigint,
    constraint ORDER_LINE_MENU_FK001 foreign key (menu_id) references menu,
    constraint ORDER_LINE_MENU_FK002 foreign key (order_id) references orders
);
