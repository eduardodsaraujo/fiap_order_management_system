package br.com.fiap.order_management.infra.gateway.db.mongo.mapper;

import br.com.fiap.order_management.domain.model.Customer;
import br.com.fiap.order_management.infra.gateway.db.mongo.document.CustomerDocument;

public class CustomerDocumentMapper {

    public static CustomerDocument toDocument(Customer customer) {
        return CustomerDocument.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .build();
    }

    public static Customer toDomain(CustomerDocument customerDocument) {
        return Customer.builder()
                .id(customerDocument.getId())
                .name(customerDocument.getName())
                .email(customerDocument.getEmail())
                .phone(customerDocument.getPhone())
                .build();
    }

}
