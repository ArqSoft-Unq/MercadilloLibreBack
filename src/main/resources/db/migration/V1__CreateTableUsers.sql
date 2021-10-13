CREATE TABLE users (
    id serial PRIMARY KEY,
    name varchar(60) NOT NULL,
    lastname varchar(60) NOT NULL,
    email varchar (60) NOT NULL UNIQUE
);
