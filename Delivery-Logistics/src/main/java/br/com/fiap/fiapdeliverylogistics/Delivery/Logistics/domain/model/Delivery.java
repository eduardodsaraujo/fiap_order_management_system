package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "orderid")
    private Long orderId;
    @Column(name = "destination_zipcode")
    private String destinationZipCode;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime lastUpdated;
    @ManyToOne
    @JoinColumn(name = "deliveryid")
    private DeliveryPerson deliveryPerson;

    public Delivery(Long orderId, DeliveryStatus status) {
        this.orderId = orderId;
        this.status = status;
    }

}

