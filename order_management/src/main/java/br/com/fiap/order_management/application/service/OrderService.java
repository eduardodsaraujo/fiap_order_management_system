package br.com.fiap.order_management.application.service;

import br.com.fiap.order_management.application.input.CreateOrderInput;
import br.com.fiap.order_management.application.input.UpdatePaymentInput;
import br.com.fiap.order_management.application.input.UpdateDeliveryAddressInput;
import br.com.fiap.order_management.domain.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order create(CreateOrderInput createOrderInput);

    Order updateDeliveryAddress(UUID orderId, UpdateDeliveryAddressInput input) throws Exception;

    Order updatePaymentMethod(UUID orderId, UpdatePaymentInput input) throws Exception;

    Order process(UUID orderId) throws Exception;

    Order findById(UUID orderId) throws Exception;

    List<Order> findAllByCustomerId(long customerId) throws Exception;

}
