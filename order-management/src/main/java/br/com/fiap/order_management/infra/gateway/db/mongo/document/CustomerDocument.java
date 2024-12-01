package br.com.fiap.order_management.infra.gateway.db.mongo.document;

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
@Document("customer")
public class CustomerDocument {

    @Id
    private Long id;
    private String name;
    private String email;
    private String phone;

}
