package br.com.fiap.order_management.api.controller;

import br.com.fiap.order_management.domain.input.CreateOrderInput;
import br.com.fiap.order_management.domain.input.UpdateDeliveryAddressInput;
import br.com.fiap.order_management.domain.input.PaymentInput;
import br.com.fiap.order_management.domain.output.OrderOutput;
import br.com.fiap.order_management.domain.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Operations related to order management")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final UpdateOrderDeliveryAddressUseCase updateOrderDeliveryAddressUseCase;
    private final OrderCheckoutUseCase orderCheckoutUseCase;
    private final UpdateOrderDeliveredUseCase updateOrderDeliveredUseCase;
    private final FindOrderByIdUseCase findOrderByIdUseCase;
    private final FindAllOrdersByCustomerIdUseCase findAllOrdersByCustomerIdUseCase;

    @PostMapping
    @Operation(summary = "Create a new order", description = "Create a new order with the provided details")
    public ResponseEntity<OrderOutput> create(@RequestBody CreateOrderInput input) {
        OrderOutput orderOutput = createOrderUseCase.execute(input);
        return ResponseEntity.ok(orderOutput);
    }

    @PutMapping(value = "/{orderId}/delivery-address")
    @Operation(summary = "Update order delivery address", description = "Update an delivery address for the specified order")
    public ResponseEntity<OrderOutput> updateDeliveryAddress(@PathVariable UUID orderId, @RequestBody UpdateDeliveryAddressInput input) {
        OrderOutput orderOutput = updateOrderDeliveryAddressUseCase.execute(orderId, input);
        return ResponseEntity.ok(orderOutput);
    }

    @PutMapping(value = "/{orderId}/checkout")
    @Operation(summary = "Checkout order", description = "Checkout the specified order")
    public ResponseEntity<OrderOutput> checkout(@PathVariable UUID orderId, @RequestBody PaymentInput input) {
        OrderOutput orderOutput = orderCheckoutUseCase.execute(orderId, input);
        return ResponseEntity.ok(orderOutput);
    }

    @PutMapping(value = "/{orderId}/delivered")
    @Operation(summary = "Delivered order", description = "Delivered an specified order")
    public ResponseEntity<Void> updateDelivered(@PathVariable UUID orderId) {
        updateOrderDeliveredUseCase.execute(orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID", description = "Retrieve order details by ID")
    public ResponseEntity<OrderOutput> findById(@PathVariable UUID orderId) throws Exception {
        OrderOutput orderOutput = findOrderByIdUseCase.execute(orderId);
        return ResponseEntity.ok(orderOutput);
    }

    @GetMapping("/by-customer/{customerId}")
    @Operation(summary = "Get all order by customer", description = "Retrieve all order for the specified customer")
    public ResponseEntity<List<OrderOutput>> findAllByCustomerId(@PathVariable long customerId) {
        List<OrderOutput> ordersOutput = findAllOrdersByCustomerIdUseCase.execute(customerId);
        return ResponseEntity.ok(ordersOutput);
    }

}
