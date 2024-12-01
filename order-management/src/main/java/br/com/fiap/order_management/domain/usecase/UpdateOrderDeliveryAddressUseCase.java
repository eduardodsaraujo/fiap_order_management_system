package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.AddressGateway;
import br.com.fiap.order_management.domain.input.UpdateDeliveryAddressInput;
import br.com.fiap.order_management.domain.mapper.OrderOutputMapper;
import br.com.fiap.order_management.domain.model.DeliveryAddress;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.gateway.DeliveryGateway;
import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.output.OrderOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateOrderDeliveryAddressUseCase {

    private final OrderGateway orderGateway;
    private final AddressGateway addressGateway;
    private final DeliveryGateway deliveryGateway;

    public OrderOutput execute(UUID orderId, UpdateDeliveryAddressInput input) {
        Order order = orderGateway.findById(orderId);
        order.validateIsOpen();

        DeliveryAddress deliveryAddress = addressGateway.findAddressByCustomerIdAndAddressId(order.getCustomer().getId(), input.getDeliveryAddressId());
        double shippingValue = deliveryGateway.calculateShipping(deliveryAddress.getPostalCode(), order.getTotalWeight());

        order.updateDeliveryAddress(deliveryAddress, shippingValue);

        order = orderGateway.save(order);

        return OrderOutputMapper.toOrderOutput(order);
    }

}
