
CREATE TABLE IF NOT EXISTS delivery (
  orderid           UUID,
  destination_zipcode   varchar(8) NOT NULL,
  status       varchar(30) NOT NULL,
  latitude     DECIMAL(9, 6),
  longitude    DECIMAL(9, 6),
  last_updated timestamp NOT NULL,
  deliverypersonid int4,
  PRIMARY KEY (orderid));


CREATE TABLE IF NOT EXISTS delivery_person (
  id           SERIAL NOT NULL,
  name         varchar(100) NOT NULL,
  vehicle_type varchar(15) NOT NULL,
  status       varchar(30) NOT NULL,
  PRIMARY KEY (id));

ALTER TABLE delivery ADD CONSTRAINT IF NOT EXISTS FKdelivery925474 FOREIGN KEY (deliverypersonid) REFERENCES delivery_person (id);

INSERT INTO delivery (
    orderid,
    destination_zipcode,
    status,
    latitude,
    longitude,
    last_updated
)
VALUES (
    '550e8400-e29b-41d4-a716-446655440000',  -- Gera um UUID único
    '12345678',          -- Exemplo de código postal (cep) de destino
    'PENDING',           -- Status inicial (PENDING)
    40.712776,           -- Latitude (exemplo)
    -74.005974,          -- Longitude (exemplo)
    CURRENT_TIMESTAMP   -- Data e hora atuais);
);

INSERT INTO delivery_person (name, vehicle_type, status) VALUES
('Carlos', 'BICYCLE', 'AVAILABLE');
