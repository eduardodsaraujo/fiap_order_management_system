package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.delivery;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.DeliveryPerson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequestDto {
    private Long orderId;
    private String destinationZipCode;
}
