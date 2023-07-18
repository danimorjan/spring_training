CREATE TABLE Stock (
product UUID REFERENCES Product(id),
location UUID REFERENCES Location(id),
quatity INTEGER,
PRIMARY KEY (product,location)
);