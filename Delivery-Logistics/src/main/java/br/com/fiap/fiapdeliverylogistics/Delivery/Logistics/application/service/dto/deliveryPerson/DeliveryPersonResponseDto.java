package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.deliveryPerson;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.DeliveryPersonStatus;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.VehicleType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPersonResponseDto {
    private Long id;
    private String nome;
    private VehicleType vehicleType;
    private DeliveryPersonStatus status;
}
