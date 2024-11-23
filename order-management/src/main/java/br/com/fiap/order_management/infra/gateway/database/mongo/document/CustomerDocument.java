package br.com.fiap.order_management.infra.gateway.database.mongo.document;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document
public class CustomerDocument {

    @Id
    private Long id;
    private String name;
    private String email;
    private String phone;

}
