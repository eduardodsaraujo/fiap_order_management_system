create table product
(
    id             serial         not null,
    code           varchar(20)    not null unique,
    name           varchar(60)    not null unique,
    description    varchar(500)   not null,
    category       varchar(30)    not null,
    manufacturer   varchar(30)    not null,
    enable         bool           not null,
    price          numeric(13, 2) not null,
    weight         numeric(10, 3) not null,
    stock_quantity numeric(15, 3) not null,
    constraint product_pk primary key (id)
);

insert into product (code, name, description, category, manufacturer, enable, price, weight, stock_quantity)
values ('IP15', 'IPhone 15', 'Iphone 15 128GB', 'Smartphone', 'Apple',
        true, 5000.0, 1.0, 100.0),
       ('IP16', 'IPhone 16', 'Iphone 16 128GB', 'Smartphone', 'Apple',
        true, 6500.0, 1.0, 100.0);