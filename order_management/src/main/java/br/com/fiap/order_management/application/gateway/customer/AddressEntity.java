package br.com.fiap.order_management.application.gateway.customer;

import br.com.fiap.order_management.domain.model.Customer;
import br.com.fiap.order_management.domain.model.DeliveryAddress;
import lombok.Getter;

@Getter
public class AddressEntity {

    private Long id;
    private String street;
    private String number;
    private String complement;
    private String district;
    private String city;
    private String state;
    private String postalCode;

    public DeliveryAddress dtoToObject(){
        return DeliveryAddress.builder()
                .id(this.id)
                .street(this.street)
                .number(this.number)
                .complement(this.complement)
                .city(this.city)
                .state(this.state)
                .postalCode(this.postalCode)
                .build();
    }

}
