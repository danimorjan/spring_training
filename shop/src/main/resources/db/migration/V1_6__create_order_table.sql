CREATE TABLE Orderr (
id UUID NOT NULL,
customer UUID REFERENCES customer(id),
createdate DATE,
address_country VARCHAR(30),
address_city VARCHAR(30),
address_county VARCHAR(30),
address_streetaddress VARCHAR(30),
PRIMARY KEY (id)
);