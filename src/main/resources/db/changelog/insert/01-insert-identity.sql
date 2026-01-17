
--liquibase formatted sql

--changeset burgasvv:1
begin ;
insert into identity(authority, username, password, email, enabled, firstname, lastname, patronymic)
values ('ADMIN', 'burgasvv','$2a$10$5boYjEElcgc7xq3KBuXiHe3/MizQBJwqdyUHgcZG9rAjEdKiQSvu2','burgasvv@gmail.com',
        true,'Бургас','Вячеслав','Васильевич');
commit ;