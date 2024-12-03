create table if not exists product
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
