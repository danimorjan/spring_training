CREATE TABLE OrderDetail (
orderr UUID REFERENCES Orderr(id),
product UUID REFERENCES product(id),
address_country VARCHAR(30),
address_city VARCHAR(30),
address_county VARCHAR(30),
address_streetaddress VARCHAR(30),
PRIMARY KEY (orderr,product)
);