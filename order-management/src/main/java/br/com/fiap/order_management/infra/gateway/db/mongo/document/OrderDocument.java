package br.com.fiap.order_management.infra.gateway.db.mongo.document;

import br.com.fiap.order_management.domain.model.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("order")
public class OrderDocument {

    @Id
    private UUID id;
    private LocalDate orderDate;
    private OrderStatus status;
    private double itemTotal;
    private double shippingValue;
    private double total;
    private double totalWeight;
    private CustomerDocument customer;
    private DeliveryAddressDocument deliveryAddress;
    private PaymentDocument payment;
    private List<OrderItemDocument> items;

}
