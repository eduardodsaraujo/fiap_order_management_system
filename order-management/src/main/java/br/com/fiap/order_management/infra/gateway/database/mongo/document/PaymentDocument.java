package br.com.fiap.order_management.infra.gateway.database.mongo.document;

import br.com.fiap.order_management.domain.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

}
