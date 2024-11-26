package br.com.fiap.customer_management.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<AddressDTO> addresses;
}
