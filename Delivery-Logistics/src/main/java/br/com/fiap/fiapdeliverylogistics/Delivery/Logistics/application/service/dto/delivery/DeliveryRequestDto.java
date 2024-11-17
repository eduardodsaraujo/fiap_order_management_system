package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.delivery;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.DeliveryPerson;
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
    private UUID orderId;
    private String destinationZipCode;
}
