package br.com.fiap.order_management.domain.output;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryAddressOutput {

    private Long id;
    private String street;
    private String number;
    private String complement;
    private String district;
    private String city;
    private String state;
    private String postalCode;

}
