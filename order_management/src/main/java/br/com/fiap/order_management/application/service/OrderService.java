package br.com.fiap.order_management.application.service;

import br.com.fiap.order_management.application.input.CreateOrderInput;
import br.com.fiap.order_management.application.input.UpdatePaymentInput;
import br.com.fiap.order_management.application.input.UpdateDeliveryAddressInput;
import br.com.fiap.order_management.domain.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order create(CreateOrderInput createOrderInput);

    Order updateDeliveryAddress(UUID orderId, UpdateDeliveryAddressInput input);

    Order updatePaymentMethod(UUID orderId, UpdatePaymentInput input);

    Order process(UUID orderId);

    Order findById(UUID orderId);

    List<Order> findAllByCustomerId(long customerId);

}
