package br.com.fiap.delivery_logistics.application.dto.deliveryPerson;

import br.com.fiap.delivery_logistics.domain.model.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPersonRequestDto {
    private String name;
    private VehicleType vehicleType;
}
