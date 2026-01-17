
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists excursion(
    id uuid default gen_random_uuid() unique not null ,
    name varchar unique not null ,
    description text unique not null ,
    price decimal not null default 0 check ( price >= 0 )
)