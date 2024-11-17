package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @Column(name = "orderid")
    private UUID orderId;
    @Column(name = "destination_zipcode")
    private String destinationZipCode;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime lastUpdated;
    @ManyToOne
    @JoinColumn(name = "deliverypersonid")
    private DeliveryPerson deliveryPerson;

    public Delivery(UUID orderId, DeliveryStatus status) {
        this.orderId = orderId;
        this.status = status;
    }

}

