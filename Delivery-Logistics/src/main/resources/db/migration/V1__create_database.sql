
CREATE TABLE delivery (
  id           SERIAL NOT NULL,
  orderid     int4 unique NOT NULL,
  status       varchar(15) NOT NULL,
  latitude     DECIMAL(9, 6),
  longitude    DECIMAL(9, 6),
  last_updated timestamp NOT NULL,
  deliveryid int4,
  PRIMARY KEY (id));


CREATE TABLE delivery_person (
  id           SERIAL NOT NULL,
  nome         varchar(100) NOT NULL,
  vehicle_type varchar(15) NOT NULL,
  status       varchar(30) NOT NULL,
  PRIMARY KEY (id));

ALTER TABLE delivery ADD CONSTRAINT FKdelivery925474 FOREIGN KEY (deliveryid) REFERENCES delivery_person (id);
