package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.deliveryPerson;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.DeliveryPersonStatus;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeDeliveryPersonStatusRequestDto {

    private Long deliveryId;
    private DeliveryPersonStatus status;
}
