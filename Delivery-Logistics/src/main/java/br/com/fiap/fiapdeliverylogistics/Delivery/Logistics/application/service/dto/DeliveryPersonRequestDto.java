package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPersonRequestDto {
    private String nome;
    private VehicleType vehicleType;
}
