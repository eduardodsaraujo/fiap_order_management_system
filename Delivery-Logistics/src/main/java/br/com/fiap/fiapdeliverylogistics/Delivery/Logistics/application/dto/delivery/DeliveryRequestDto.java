package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.dto.delivery;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.DeliveryPerson;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
