package br.com.fiap.delivery_logistics.application.dto.delivery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequestDto {
    @NotNull(message = "Order ID cannot be null")
    private UUID orderId;

    @NotNull(message = "Destination ZIP code cannot be null")
    @Pattern(regexp = "\\d{8}", message = "Destination ZIP code must contain exactly 8 digits")
    private String destinationZipCode;
}
