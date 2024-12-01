package br.com.fiap.order_management.infra.gateway.db.mongo.document;

import br.com.fiap.order_management.domain.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("payment")
public class PaymentDocument {

    @Id
    private UUID id;
    private PaymentMethod paymentMethod;
    private double value;
    private LocalDateTime paymentTimestamp;
    private String status;
    private String authNumber;
}
