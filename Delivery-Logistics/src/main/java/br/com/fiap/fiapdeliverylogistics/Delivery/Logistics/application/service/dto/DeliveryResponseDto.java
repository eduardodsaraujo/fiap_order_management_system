package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.DeliveryPerson;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.DeliveryStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponseDto {
    private Long id;
    private Long orderId;
    private DeliveryStatus status;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime lastUpdated;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private DeliveryPersonResponseDto deliveryPerson;
}
