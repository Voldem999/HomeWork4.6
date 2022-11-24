create table car
(
    id    serial primary key,
    brand text          not null,
    model text          not null,
    price numeric(9, 2) not null
);

create table human
(
    id             serial primary key,
    name           text                      not null,
    age            integer check ( age > 0 ) not null,
    driver_license boolean                   not null,
    car_id         integer references car (id)
);