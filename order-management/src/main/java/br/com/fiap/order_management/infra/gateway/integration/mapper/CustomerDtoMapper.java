package br.com.fiap.order_management.infra.gateway.integration.mapper;

import br.com.fiap.order_management.domain.model.Customer;
import br.com.fiap.order_management.infra.gateway.integration.dto.customer.CustomerDto;

public class CustomerDtoMapper {

    public static Customer toDomain(CustomerDto customerDto){
        return Customer.builder()
                .id(customerDto.getId())
                .name(customerDto.getName())
                .email(customerDto.getEmail())
                .phone(customerDto.getPhone())
                .build();
    }

}
