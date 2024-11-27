
CREATE TABLE delivery (
  orderid           UUID,
  destination_zipcode   varchar(8) NOT NULL,
  status       varchar(30) NOT NULL,
  latitude     DECIMAL(9, 6),
  longitude    DECIMAL(9, 6),
  last_updated timestamp NOT NULL,
  deliverypersonid int4,
  PRIMARY KEY (orderid));


CREATE TABLE delivery_person (
  id           SERIAL NOT NULL,
  nome         varchar(100) NOT NULL,
  vehicle_type varchar(15) NOT NULL,
  status       varchar(30) NOT NULL,
  PRIMARY KEY (id));

ALTER TABLE delivery ADD CONSTRAINT FKdelivery925474 FOREIGN KEY (deliverypersonid) REFERENCES delivery_person (id);
