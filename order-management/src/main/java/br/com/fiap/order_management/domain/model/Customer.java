package br.com.fiap.order_management.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Customer {

    private Long id;
    private String name;
    private String email;
    private String phone;

}
