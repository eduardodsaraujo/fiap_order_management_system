package br.com.fiap.fiapcustomermanagement.Customer.Management.application.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerRequestDTO {

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be valid.")
    private String email;

    @Pattern(regexp = "\\d{10,15}", message = "Phone number must be between 10 and 15 digits.")
    private String phone;
}
