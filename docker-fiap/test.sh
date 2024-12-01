# Create Customer
curl -X 'POST' \
'http://localhost:8080/customer-management/api/customers' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-d '{
  "name": "JOAO",
  "email": "joao@test.com",
  "phone": "11999995555"
}'
{
  "name": "JOAO",
  "email": "joao@test.com",
  "phone": "11999995555"
}

# Create Address
curl -X 'POST' \
  'http://localhost:8080/customer-management/api/customers/1/addresses' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "street": "Av. Paulista",
  "number": "1106",
  "complement": "7 Andar",
  "district": "Bela Vista",
  "city": "São Paulo",
  "state": "SP",
  "postalCode": "01311000"
}'

{
  "street": "Av. Paulista",
  "number": "1106",
  "complement": "7 Andar",
  "district": "Bela Vista",
  "city": "São Paulo",
  "state": "SP",
  "postalCode": "01311000"
}


# Create Product
curl -X 'POST' \
  'http://localhost:8080/product-management/api/products' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "code": "IP16P",
  "name": "IPhone 16 Pro",
  "description": "IPhone 16 Pro 128GB Apple",
  "category": "Smartphone",
  "manufacturer": "Apple",
  "price": 9000.00,
  "weight": 1.0,
  "stockQuantity": 100
}'

{
  "code": "IP16P",
  "name": "IPhone 16 Pro",
  "description": "IPhone 16 Pro 128GB Apple",
  "category": "Smartphone",
  "manufacturer": "Apple",
  "price": 9000.00,
  "weight": 1.0,
  "stockQuantity": 100
}




# Create Orders
curl -X 'POST' \
  'http://localhost:8080/order-management/api/orders' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "customerId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}'

{
  "customerId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
# c8fcf9d2-a789-4227-9eff-788203a547b3
# 3c614b85-d4b4-42cf-87b0-5e160c891732
# Select Delivery Address
curl -X 'PUT' \
  'http://localhost:8080/order-management/api/orders/c8fcf9d2-a789-4227-9eff-788203a547b3/delivery-address' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "deliveryAddressId": 1
}'
{
  "deliveryAddressId": 1
}


curl -X 'POST' \
  'http://localhost:8080/order-management/api/payment/callback/3c614b85-d4b4-42cf-87b0-5e160c891732' \
  -H 'accept: */*' \
  -d ''

