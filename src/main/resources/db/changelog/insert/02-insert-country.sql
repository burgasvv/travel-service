
--liquibase formatted sql

--changeset burgasvv:1
begin ;
insert into country(name, description) values ('Россия','Описание страны Россия');
insert into country(name, description) values ('США','Описание страны США');
insert into country(name, description) values ('КНДР','Описание страны КНДР');
commit ;