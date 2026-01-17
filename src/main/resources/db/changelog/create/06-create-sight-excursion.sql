
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists sight_excursion(
    sight_id uuid references sight(id) on delete cascade on update cascade ,
    excursion_id uuid references excursion(id) on delete cascade on update cascade
)