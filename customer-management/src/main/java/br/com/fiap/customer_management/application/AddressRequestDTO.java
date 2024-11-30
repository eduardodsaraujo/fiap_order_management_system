package br.com.fiap.customer_management.application;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestDTO {
    @NotBlank(message = "Street is required.")
    @Size(max = 60, message = "Street must not exceed 60 characters.")
    private String street;

    @NotBlank(message = "Number is required.")
    @Size(max = 10, message = "Number must not exceed 10 characters.")
    private String number;

    @Size(max = 60, message = "Complement must not exceed 60 characters.")
    private String complement;

    @NotBlank(message = "District is required.")
    @Size(max = 30, message = "District must not exceed 30 characters.")
    private String district;

    @NotBlank(message = "City is required.")
    @Size(max = 30, message = "City must not exceed 30 characters.")
    private String city;

    @NotBlank(message = "State is required.")
    @Size(min = 2, max = 2, message = "State must be exactly 2 characters.")
    private String state;

    @NotBlank(message = "Postal code is required.")
    @Size(min = 8, max = 8, message = "Postal code must be exactly 8 characters.")
    private String postalCode;
}
