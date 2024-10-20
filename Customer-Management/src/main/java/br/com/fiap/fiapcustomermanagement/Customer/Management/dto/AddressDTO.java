package br.com.fiap.fiapcustomermanagement.Customer.Management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDTO {
    private Long id;
    private String street;
    private String number;
    private String complement;
    private String district;
    private String city;
    private String state;
    private String postalCode;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long customerId;
}
