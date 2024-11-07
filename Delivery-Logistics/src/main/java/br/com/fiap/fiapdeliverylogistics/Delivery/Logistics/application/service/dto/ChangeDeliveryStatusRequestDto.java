package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeDeliveryStatusRequestDto {

    private Long deliveryId;
    private DeliveryStatus status;

}
