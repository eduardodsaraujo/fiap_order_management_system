package br.com.fiap.order_management.gateway.customer;

import br.com.fiap.order_management.domain.model.Customer;
import lombok.Getter;

@Getter
public class CustomerEntity {

    private Long id;
    private String name;
    private String email;
    private String phone;

    public Customer dtoToObject(){
        return Customer.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .build();
    }

}
