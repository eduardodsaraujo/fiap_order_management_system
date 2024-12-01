package br.com.fiap.delivery_logistics.application.dto.delivery;

import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonResponseDto;
import br.com.fiap.delivery_logistics.domain.model.DeliveryStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponseDto {
    private UUID orderId;
    private String destinationZipCode;
    private DeliveryStatus status;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal latitude;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal longitude;
    private LocalDateTime lastUpdated;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private DeliveryPersonResponseDto deliveryPerson;
}
