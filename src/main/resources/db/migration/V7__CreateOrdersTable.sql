CREATE TABLE orders (
  id serial PRIMARY KEY,
  status varchar(30),
  buyer_id integer NOT NULL references users(id)
);
