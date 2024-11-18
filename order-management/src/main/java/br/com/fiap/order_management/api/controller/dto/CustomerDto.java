package br.com.fiap.order_management.api.controller.dto;

import br.com.fiap.order_management.domain.model.Customer;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerDto {

    private Long id;
    private String name;
    private String email;
    private String phone;

    public static CustomerDto toDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .build();
    }

}
