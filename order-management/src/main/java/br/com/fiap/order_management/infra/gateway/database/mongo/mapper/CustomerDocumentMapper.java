package br.com.fiap.order_management.infra.gateway.database.mongo.mapper;

import br.com.fiap.order_management.domain.model.Customer;
import br.com.fiap.order_management.infra.gateway.database.mongo.document.CustomerDocument;

public class CustomerDocumentMapper {

    public static CustomerDocument toDocument(Customer customer) {
        if (customer != null) {
            return CustomerDocument.builder()
                    .id(customer.getId())
                    .name(customer.getName())
                    .email(customer.getEmail())
                    .phone(customer.getPhone())
                    .build();
        }
        return null;
    }

    public static Customer toDomain(CustomerDocument customerDocument) {
        if (customerDocument != null) {
            return Customer.builder()
                    .id(customerDocument.getId())
                    .name(customerDocument.getName())
                    .email(customerDocument.getEmail())
                    .phone(customerDocument.getPhone())
                    .build();
        }
        return null;
    }

}
