ALTER TABLE orders
    ADD COLUMN user_id UUID NOT NULL;

CREATE INDEX orders_user_id_idx ON orders (user_id);
