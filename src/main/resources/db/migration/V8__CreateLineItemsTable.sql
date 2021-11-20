CREATE TABLE line_items (
  id serial PRIMARY KEY,
  price integer NOT NULL,
  order_id integer NOT NULL references orders(id),
  product_id integer NOT NULL references products(id)
);
