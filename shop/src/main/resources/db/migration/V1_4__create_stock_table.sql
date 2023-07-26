CREATE TABLE Stock (
product UUID REFERENCES Product(id),
location UUID REFERENCES Location(id),
quantity INTEGER,
PRIMARY KEY (product,location)
);