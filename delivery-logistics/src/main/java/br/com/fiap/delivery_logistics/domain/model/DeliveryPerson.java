package br.com.fiap.delivery_logistics.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class DeliveryPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
    @Enumerated(EnumType.STRING)
    private DeliveryPersonStatus status;
    @OneToMany(mappedBy = "deliveryPerson")
    private List<Delivery> deliveries;  // One-to-Many relationship with Delivery

    public DeliveryPerson(String name, VehicleType vehicleType) {
        this.name = name;
        this.vehicleType = vehicleType;
    }
}
