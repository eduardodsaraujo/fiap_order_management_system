package br.com.fiap.order_management.infra.gateway.integration.dto.customer;

import lombok.Data;

@Data
public class AddressDto {

    private Long id;
    private String street;
    private String number;
    private String complement;
    private String district;
    private String city;
    private String state;
    private String postalCode;

}
