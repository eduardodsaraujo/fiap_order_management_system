package br.com.fiap.customer_management.utils;

import br.com.fiap.customer_management.application.AddressDTO;
import br.com.fiap.customer_management.application.AddressRequestDTO;
import br.com.fiap.customer_management.domain.model.Address;
import br.com.fiap.customer_management.domain.model.Customer;

public class AddressHelper {

    public static Address createAddress() {
        var address = new Address();
        return Address.builder()
                .street("Rua das Flores")
                .number("123")
                .complement("Apto 45")
                .district("Centro")
                .city("São Paulo")
                .state("SP")
                .postalCode("01001000")
                .build();
    }

    public static Address createAddress(Long addressId, Customer customer) {
        return Address.builder()
                .id(addressId)
                .street("Rua das Flores")
                .number("123")
                .complement("Apto 45")
                .district("Centro")
                .city("São Paulo")
                .state("SP")
                .postalCode("01001000")
                .customer(customer)
                .build();
    }

    public static AddressRequestDTO createAddressRequestDTO() {
        return AddressRequestDTO.builder()
                .street("Rua das Flores")
                .number("123")
                .complement("Apto 45")
                .district("Centro")
                .city("São Paulo")
                .state("SP")
                .postalCode("01001000")
                .build();
    }
    public static AddressDTO createAddressDTO(Long id) {
        return AddressDTO.builder()
                .id(id)
                .street("Rua das Flores")
                .number("123")
                .complement("Apto 45")
                .district("Centro")
                .city("São Paulo")
                .state("SP")
                .postalCode("01001000")
                .build();
    }

}
