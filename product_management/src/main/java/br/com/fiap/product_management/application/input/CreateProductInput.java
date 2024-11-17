package br.com.fiap.product_management.application.input;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateProductInput {

    @NotBlank(message = "Code is required.")
    @Size(max = 20, message = "Code must not exceed 20 characters.")
    private String code;
    @NotBlank(message = "Name is required.")
    @Size(max = 60, message = "Name must not exceed 60 characters.")
    private String name;
    @NotBlank(message = "Description is required.")
    @Size(max = 500, message = "Description must not exceed 500 characters.")
    private String description;
    @NotBlank(message = "Category is required.")
    @Size(max = 30, message = "Category must not exceed 30 characters.")
    private String category;
    @NotBlank(message = "Manufacturer is required.")
    @Size(max = 30, message = "Manufacturer must not exceed 30 characters.")
    private String manufacturer;
    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    private double price;
    @DecimalMin(value = "0.0", message = "Weight cannot be negative")
    private double weight;
    @DecimalMin(value = "0.0", message = "Stock quantity cannot be negative")
    private double stockQuantity;

}
