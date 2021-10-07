create database selic;

select 'create database selic'
    where not exists (select from pg_database where datname = 'selic')\gexec

select 'create table produto
(
    codigo bigserial
        constraint produto_pkey
            primary key,
    name   varchar(30)    not null,
    price  numeric(11, 2) not null
);

alter table produto
    owner to postgres;'
    where not exists (select from pg_)

-- SQL para inserção de dados (não utilizado por padrão)
-- insert into produto (name, price)
-- values ('Colar de Prata', 112.25);
--
-- insert into produto (name, price)
-- values ('Colar de Ouro', 302.00);
--
-- insert into produto (name, price)
-- values ('Colar de Diamante', 1599.99);
--
-- insert into produto (name, price)
-- values ('Colar de Madeira', 39.79);create table public.produto (codigo  bigserial not null, name varchar(30) not null, price numeric(11, 2) not null, primary key (codigo));
