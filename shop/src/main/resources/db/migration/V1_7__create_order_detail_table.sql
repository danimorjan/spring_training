CREATE TABLE IF NOT EXISTS OrderDetail (
    order_id UUID REFERENCES orderr(id),
    product_id UUID REFERENCES product(id),
    shipped_from UUID REFERENCES location(id),
    quantity integer,
    PRIMARY KEY (order_id, product_id)
);