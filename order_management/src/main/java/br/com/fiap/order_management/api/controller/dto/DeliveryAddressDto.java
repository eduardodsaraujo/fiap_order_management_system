package br.com.fiap.order_management.api.controller.dto;

import br.com.fiap.order_management.domain.model.DeliveryAddress;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryAddressDto {

    private Long id;
    private String street;
    private String number;
    private String complement;
    private String district;
    private String city;
    private String state;
    private String postalCode;

    public static DeliveryAddressDto toDto(DeliveryAddress address){
        return DeliveryAddressDto.builder()
                .id(address.getId())
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .build();
    }

}
