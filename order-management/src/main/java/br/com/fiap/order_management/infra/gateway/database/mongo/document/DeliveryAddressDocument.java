package br.com.fiap.order_management.infra.gateway.database.mongo.document;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document
public class DeliveryAddressDocument {

    @Id
    private Long id;
    private String street;
    private String number;
    private String complement;
    private String district;
    private String city;
    private String state;
    private String postalCode;

}
