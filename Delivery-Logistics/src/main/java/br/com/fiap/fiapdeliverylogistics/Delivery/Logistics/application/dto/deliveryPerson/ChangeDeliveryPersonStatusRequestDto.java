package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.dto.deliveryPerson;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.DeliveryPersonStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeDeliveryPersonStatusRequestDto {

    private Long deliveryPersonId;
    private DeliveryPersonStatus status;
}
