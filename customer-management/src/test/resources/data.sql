CREATE TABLE address (
  id          SERIAL NOT NULL,
  street      varchar(60) NOT NULL,
  number      varchar(10) NOT NULL,
  complement  varchar(60),
  district    varchar(30) NOT NULL,
  city        varchar(30) NOT NULL,
  state       varchar(2) NOT NULL,
  postal_code varchar(8) NOT NULL,
  Customerid  int4 NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE customer (
  id    SERIAL NOT NULL,
  name  varchar(100) NOT NULL,
  email varchar(100) NOT NULL UNIQUE,
  phone varchar(15),
  PRIMARY KEY (id));

ALTER TABLE address ADD CONSTRAINT FKaddress232379 FOREIGN KEY (Customerid) REFERENCES customer (id);

INSERT INTO customer (name, email, phone) VALUES ('Fernando', 'fernando@gmail.com', '21982405000');
