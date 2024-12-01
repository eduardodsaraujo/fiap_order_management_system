package br.com.fiap.delivery_logistics.application.dto.delivery;

import br.com.fiap.delivery_logistics.domain.model.DeliveryStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class ChangeDeliveryStatusRequestDto {
    @NotNull(message = "Delivery ID cannot be null")
    private UUID deliveryId;
    @NotNull(message = "Delivery status cannot be null")
    private DeliveryStatus status;

}
