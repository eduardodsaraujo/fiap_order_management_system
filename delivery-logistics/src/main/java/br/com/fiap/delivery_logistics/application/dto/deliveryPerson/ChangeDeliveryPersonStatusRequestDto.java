package br.com.fiap.delivery_logistics.application.dto.deliveryPerson;

import br.com.fiap.delivery_logistics.domain.model.DeliveryPersonStatus;
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