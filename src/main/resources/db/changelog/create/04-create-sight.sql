
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists sight(
    id uuid default gen_random_uuid() unique not null ,
    name varchar unique not null ,
    description text unique not null ,
    city_id uuid references city(id) on delete set null on update cascade
)