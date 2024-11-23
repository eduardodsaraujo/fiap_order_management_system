package br.com.fiap.order_management.infra.gateway.database.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("product")
public class ProductDocument {

    @Id
    private long id;
    private String code;
    private String description;
    private double price;
    private double weight;

}
