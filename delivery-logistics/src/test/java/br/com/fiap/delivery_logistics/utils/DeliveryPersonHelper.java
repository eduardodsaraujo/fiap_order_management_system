package br.com.fiap.delivery_logistics.utils;

import br.com.fiap.delivery_logistics.domain.model.DeliveryPerson;
import br.com.fiap.delivery_logistics.domain.model.DeliveryPersonStatus;
import br.com.fiap.delivery_logistics.domain.model.VehicleType;

public class DeliveryPersonHelper {

    public static DeliveryPerson createDeliveryPerson(DeliveryPersonStatus status) {
        return DeliveryPerson.builder()
                .name("Juninho")
                .vehicleType(VehicleType.BICYCLE)
                .status(status)
                .build();
    }

}
