package br.com.fiap.order_management.infra.gateway.db.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("order-item")
public class OrderItemDocument {

    @Id
    private UUID id;
    private double quantity;
    private ProductDocument product;

}
