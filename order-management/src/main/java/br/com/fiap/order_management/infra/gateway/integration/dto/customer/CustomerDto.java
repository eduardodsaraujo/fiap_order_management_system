package br.com.fiap.order_management.infra.gateway.integration.dto.customer;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerDto {

    private Long id;
    private String name;
    private String email;
    private String phone;

}
