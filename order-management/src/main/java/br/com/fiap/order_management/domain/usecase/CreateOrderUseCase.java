package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.mapper.OrderOutputMapper;
import br.com.fiap.order_management.domain.output.OrderOutput;
import br.com.fiap.order_management.domain.input.CreateOrderInput;
import br.com.fiap.order_management.domain.input.OrderItemInput;
import br.com.fiap.order_management.domain.model.Customer;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.OrderItem;
import br.com.fiap.order_management.domain.model.Product;
import br.com.fiap.order_management.domain.gateway.CustomerGateway;
import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.gateway.ProductGateway;
import br.com.fiap.order_management.infra.exception.OrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final OrderGateway orderGateway;
    private final CustomerGateway customerGateway;
    private final ProductGateway productGateway;

    @Transactional
    public OrderOutput execute(CreateOrderInput input) {
        Customer customer = customerGateway.findById(input.getCustomerId());
        List<Product> products = productGateway.findAllByIds(input.getItems().stream().map(OrderItemInput::getProductId).toList());

        List<OrderItem> items = new ArrayList<>();
        for (OrderItemInput itemInput : input.getItems()) {
            Product product = products.stream().filter(p -> p.getId() == itemInput.getProductId()).findFirst()
                    .orElseThrow(() -> new OrderException("Product not found"));

            items.add(OrderItem.builder()
                    .id(UUID.randomUUID())
                    .quantity(itemInput.getQuantity())
                    .product(product)
                    .build());
        }

        Order order = Order.builder()
                .customer(customer)
                .items(items)
                .build();
        order.create();

        order = orderGateway.save(order);

        return OrderOutputMapper.toOrderOutput(order);
    }

}
