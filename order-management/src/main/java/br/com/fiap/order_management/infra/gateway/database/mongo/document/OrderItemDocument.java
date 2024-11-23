package br.com.fiap.order_management.infra.gateway.database.mongo.document;

import br.com.fiap.order_management.domain.model.Product;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Builder
@Document
public class OrderItemDocument {

    @Id
    private UUID id;
    private double quantity;
    private ProductDocument product;

}
