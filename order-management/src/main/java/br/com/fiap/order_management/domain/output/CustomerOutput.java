package br.com.fiap.order_management.domain.output;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerOutput {

    private Long id;
    private String name;
    private String email;
    private String phone;

}
