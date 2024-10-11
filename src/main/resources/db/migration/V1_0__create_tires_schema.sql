CREATE SCHEMA IF NOT EXISTS tires;

CREATE TABLE IF NOT EXISTS tires.options
(
    id    bigserial PRIMARY KEY,
    name  varchar(255)  NOT NULL UNIQUE,
    price numeric(8, 2) NOT NULL,
    deleted boolean default false
);

CREATE TABLE IF NOT EXISTS tires.customers
(
    login              varchar(255) PRIMARY KEY,
    name               varchar(255) NOT NULL,
    phone_number       varchar(255) NOT NULL UNIQUE,
    car_model          varchar(255) NOT NULL,
    dimension_of_tires varchar(255) NOT NULL,
    deleted boolean default false
);

CREATE TABLE IF NOT EXISTS tires.orders
(
    id             bigserial PRIMARY KEY,
    customer_login varchar(255) NOT NULL REFERENCES customers (login) ON DELETE SET NULL,
    creation_time timestamp with time zone,
    deleted boolean default false
);

CREATE TABLE IF NOT EXISTS tires.order_option (
    order_id bigserial NOT NULL REFERENCES orders (id) ON DELETE SET NULL,
    option_id bigserial NOT NULL REFERENCES options (id) ON DELETE SET NULL
);