package br.com.fiap.order_management.infra.gateway.integration.dto.customer;

import lombok.Getter;

@Getter
public class CustomerDto {

    private Long id;
    private String name;
    private String email;
    private String phone;

}
