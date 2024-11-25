package br.com.fiap.order_management.domain.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateDeliveryAddressInput {

    private long deliveryAddressId;

}
