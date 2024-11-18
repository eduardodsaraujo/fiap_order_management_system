package br.com.fiap.delivery_logistics.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DeliveryPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
    @Enumerated(EnumType.STRING)
    private DeliveryPersonStatus status;
    @OneToMany(mappedBy = "deliveryPerson")
    private List<Delivery> deliveries;  // One-to-Many relationship with Delivery

    public DeliveryPerson(String nome, VehicleType vehicleType) {
        this.nome = nome;
        this.vehicleType = vehicleType;
    }
}
