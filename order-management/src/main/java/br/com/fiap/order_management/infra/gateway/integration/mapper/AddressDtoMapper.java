package br.com.fiap.order_management.infra.gateway.integration.mapper;

import br.com.fiap.order_management.domain.model.DeliveryAddress;
import br.com.fiap.order_management.infra.gateway.integration.dto.customer.AddressDto;

public class AddressDtoMapper {

    public static DeliveryAddress toDomain(AddressDto addressDto){
        return DeliveryAddress.builder()
                .id(addressDto.getId())
                .street(addressDto.getStreet())
                .number(addressDto.getNumber())
                .complement(addressDto.getComplement())
                .city(addressDto.getCity())
                .state(addressDto.getState())
                .postalCode(addressDto.getPostalCode())
                .build();
    }

}
