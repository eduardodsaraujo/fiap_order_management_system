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
    @NotBlank(message = "Destination ZIP Code cannot be blank")
    @Pattern(
            regexp = "\\d{5}-\\d{3}",
            message = "Destination ZIP Code must follow the format XXXXX-XXX"
    )
    private String destinationZipCode;
}
