package br.com.fiap.order_management.api.controller;

import br.com.fiap.order_management.domain.input.CreateOrderInput;
import br.com.fiap.order_management.domain.input.UpdateDeliveryAddressInput;
import br.com.fiap.order_management.domain.input.UpdatePaymentInput;
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
    private final UpdateOrderPaymentMethodUseCase updateOrderPaymentMethodUseCase;
    private final UpdateOrderDeliveredUseCase updateOrderDeliveredUseCase;
    private final ProcessOrderUseCase processOrderUseCase;
    private final FindOrderByIdUseCase findOrderByIdUseCase;
    private final FindAllOrdersByCustomerIdUseCase findAllOrdersByCustomerIdUseCase;

    @PostMapping
    @Operation(summary = "Create a new order", description = "Create a new order with the provided details")
    public ResponseEntity<OrderOutput> create(@RequestBody CreateOrderInput input) {
        OrderOutput orderOutput = createOrderUseCase.create(input);
        return ResponseEntity.ok(orderOutput);
    }

    @PutMapping(value = "/{orderId}/delivery-address")
    @Operation(summary = "Update order delivery address", description = "Update an delivery address for the specified order")
    public ResponseEntity<OrderOutput> updateDeliveryAddress(@PathVariable UUID orderId, @RequestBody UpdateDeliveryAddressInput input) {
        OrderOutput orderOutput = updateOrderDeliveryAddressUseCase.update(orderId, input);
        return ResponseEntity.ok(orderOutput);
    }

    @PutMapping(value = "/{orderId}/payment")
    @Operation(summary = "Update order payment", description = "Update an payment for the specified order")
    public ResponseEntity<OrderOutput> updatePayment(@PathVariable UUID orderId, @RequestBody UpdatePaymentInput input) {
        OrderOutput orderOutput = updateOrderPaymentMethodUseCase.update(orderId, input);
        return ResponseEntity.ok(orderOutput);
    }

    @PutMapping(value = "/{orderId}/process")
    @Operation(summary = "Process order", description = "Process an specified order")
    public ResponseEntity<OrderOutput> process(@PathVariable UUID orderId) {
        OrderOutput orderOutput = processOrderUseCase.process(orderId);
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
        OrderOutput orderOutput = findOrderByIdUseCase.findById(orderId);
        return ResponseEntity.ok(orderOutput);
    }

    @GetMapping("/by-customer/{customerId}")
    @Operation(summary = "Get all order by customer", description = "Retrieve all order for the specified customer")
    public ResponseEntity<List<OrderOutput>> findAllByCustomerId(@PathVariable long customerId) {
        List<OrderOutput> ordersOutput = findAllOrdersByCustomerIdUseCase.findAll(customerId);
        return ResponseEntity.ok(ordersOutput);
    }

}
