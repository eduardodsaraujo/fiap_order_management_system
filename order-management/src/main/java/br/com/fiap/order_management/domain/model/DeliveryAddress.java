package br.com.fiap.order_management.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryAddress {

    private Long id;
    private String street;
    private String number;
    private String complement;
    private String district;
    private String city;
    private String state;
    private String postalCode;

}
