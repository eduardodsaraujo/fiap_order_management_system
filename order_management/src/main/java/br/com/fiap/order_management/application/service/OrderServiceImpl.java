package br.com.fiap.order_management.application.service;

import br.com.fiap.order_management.application.gateway.customer.AddressEntity;
import br.com.fiap.order_management.application.gateway.customer.AddressGateway;
import br.com.fiap.order_management.application.gateway.customer.CustomerEntity;
import br.com.fiap.order_management.application.gateway.customer.CustomerGateway;
import br.com.fiap.order_management.application.gateway.product.ProductEntity;
import br.com.fiap.order_management.application.gateway.product.ProductGateway;
import br.com.fiap.order_management.application.input.CreateOrderInput;
import br.com.fiap.order_management.application.input.OrderItemInput;
import br.com.fiap.order_management.application.input.UpdatePaymentInput;
import br.com.fiap.order_management.application.input.UpdateDeliveryAddressInput;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.OrderItem;
import br.com.fiap.order_management.domain.model.Payment;
import br.com.fiap.order_management.domain.repository.OrderRepository;
import br.com.fiap.order_management.infra.exception.OrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final CustomerGateway customerGateway;
    private final ProductGateway productGateway;
    private final AddressGateway addressGateway;

    @Override
    public Order create(CreateOrderInput input) {
        CustomerEntity customerEntity = customerGateway.findById(input.getCustomerId());
        List<ProductEntity> products = productGateway.findAllByIds(input.getItems().stream().map(OrderItemInput::getProductId).toList());

        List<OrderItem> items = new ArrayList<>();
        for (OrderItemInput itemInput : input.getItems()) {
            ProductEntity productEntity = products.stream().filter(p -> p.getId() == itemInput.getProductId()).findFirst()
                    .orElseThrow(() -> new OrderException("Order not found"));

            items.add(OrderItem.builder()
                    .id(UUID.randomUUID())
                    .quantity(itemInput.getQuantity())
                    .product(productEntity.dtoToObject())
                    .build());
        }

        Order order = Order.builder()
                .customer(customerEntity.dtoToObject())
                .items(items)
                .build();
        order.create();

        orderRepository.save(order);

        return order;
    }

    @Override
    public Order updateDeliveryAddress(UUID orderId, UpdateDeliveryAddressInput input) throws Exception {
        Order order = findById(orderId);
        AddressEntity addressEntity = addressGateway.findByCustomerIdAndAddressId(order.getCustomer().getId(), input.getDeliveryAddressId());
        double shippingValue = 10.0; // TODO DELIVERY SERVICE

        order.updateDeliveryAddress(addressEntity.dtoToObject(), shippingValue);

        orderRepository.save(order);

        return order;
    }

    @Override
    public Order updatePaymentMethod(UUID orderId, UpdatePaymentInput input) throws Exception {
        Order order = findById(orderId);
        Payment payment = Payment.builder()
                .paymentMethod(input.getPaymentMethod())
                .value(order.getTotal())
                .build();

        order.updatePayment(payment);

        orderRepository.save(order);
        return order;
    }

    @Override
    public Order process(UUID orderId) throws Exception {
        Order order = findById(orderId);
        order.process();

        // Process product stock
        for (OrderItem item : order.getItems()) {
            productGateway.decreaseStock(item.getId());
        }

        // Process payment
        order.processPayment();

        // Process delivery

        return order;
    }

    @Override
    public Order findById(UUID orderId) throws Exception {
        return orderRepository.findById(orderId).orElseThrow(() -> new Exception());
    }

}
