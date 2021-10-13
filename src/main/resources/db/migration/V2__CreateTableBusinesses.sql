CREATE TABLE businesses (
    id serial PRIMARY KEY,
    name varchar(60) NOT NULL,
    email varchar(60) NOT NULL UNIQUE
);
