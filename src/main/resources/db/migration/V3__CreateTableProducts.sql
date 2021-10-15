CREATE TABLE products (
    id serial PRIMARY KEY,
    name varchar(60) NOT NULL,
    description varchar(60) NOT NULL,
    price integer NOT NULL,
    stock integer NOT NULL,
    seller_id integer NOT NULL references businesses(id)
);
