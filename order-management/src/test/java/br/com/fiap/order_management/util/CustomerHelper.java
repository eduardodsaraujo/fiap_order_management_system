package br.com.fiap.order_management.util;

import br.com.fiap.order_management.domain.model.Customer;
import br.com.fiap.order_management.domain.model.DeliveryAddress;
import br.com.fiap.order_management.domain.output.CustomerOutput;
import br.com.fiap.order_management.infra.gateway.db.mongo.document.CustomerDocument;
import br.com.fiap.order_management.infra.gateway.db.mongo.document.DeliveryAddressDocument;
import br.com.fiap.order_management.infra.gateway.integration.dto.customer.AddressDto;
import br.com.fiap.order_management.infra.gateway.integration.dto.customer.CustomerDto;

public class CustomerHelper {

    public static Customer createCustomer() {
        return Customer.builder()
                .id(1L)
                .name("JOAO")
                .email("joao@test.com.br")
                .phone("123456789")
                .build();
    }

    public static CustomerDocument createCustomerDocument() {
        return CustomerDocument.builder()
                .id(1L)
                .name("JOAO")
                .email("joao@test.com.br")
                .phone("123456789")
                .build();
    }

    public static CustomerOutput createCustomerOutput() {
        return CustomerOutput.builder()
                .id(1L)
                .name("JOAO")
                .email("joao@test.com.br")
                .phone("123456789")
                .build();
    }


    public static CustomerDto createCustomerDto() {
        return CustomerDto.builder()
                .id(1L)
                .name("JOAO")
                .email("joao@test.com.br")
                .phone("123456789")
                .build();
    }

    public static DeliveryAddress createDeliveryAddress() {
        return DeliveryAddress.builder()
                .id(1L)
                .street("AV TESTE")
                .postalCode("12345000")
                .build();
    }

    public static DeliveryAddressDocument createDeliveryAddressDocument() {
        return DeliveryAddressDocument.builder()
                .id(1L)
                .street("AV TESTE")
                .postalCode("12345000")
                .build();
    }

    public static AddressDto createAddressDto() {
        return AddressDto.builder()
                .id(1L)
                .street("AV TESTE")
                .postalCode("12345000")
                .build();
    }

}
