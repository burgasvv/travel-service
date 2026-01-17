
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists city(
    id uuid default gen_random_uuid() unique not null ,
    name varchar unique not null ,
    description text unique not null ,
    country_id uuid references country(id) on delete set null on update cascade
)