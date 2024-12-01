package br.com.fiap.delivery_logistics.application.dto.deliveryPerson;

import br.com.fiap.delivery_logistics.domain.model.DeliveryPersonStatus;
import br.com.fiap.delivery_logistics.domain.model.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPersonResponseDto {
    private Long id;
    private String name;
    private VehicleType vehicleType;
    private DeliveryPersonStatus status;
}
