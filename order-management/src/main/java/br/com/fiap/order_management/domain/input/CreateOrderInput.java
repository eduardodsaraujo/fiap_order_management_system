package br.com.fiap.order_management.domain.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderInput {

    private long customerId;
    private List<OrderItemInput> items;

}
