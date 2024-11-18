package br.com.fiap.order_management.api.controller;

import br.com.fiap.order_management.api.controller.dto.OrderDto;
import br.com.fiap.order_management.application.input.CreateOrderInput;
import br.com.fiap.order_management.application.input.UpdateDeliveryAddressInput;
import br.com.fiap.order_management.application.input.UpdatePaymentInput;
import br.com.fiap.order_management.application.service.OrderService;
import br.com.fiap.order_management.domain.model.Order;
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

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order", description = "Create a new order with the provided details")
    public ResponseEntity<OrderDto> create(@RequestBody CreateOrderInput input) {
        Order order = orderService.create(input);
        return ResponseEntity.ok(OrderDto.toDto(order));
    }

    @PutMapping(value = "/{orderId}/delivery-address")
    @Operation(summary = "Update order delivery address", description = "Update an delivery address for the specified order")
    public ResponseEntity<OrderDto> updateDeliveryAddress(@PathVariable UUID orderId, @RequestBody UpdateDeliveryAddressInput input) throws Exception {
        Order order = orderService.updateDeliveryAddress(orderId, input);
        return ResponseEntity.ok(OrderDto.toDto(order));
    }

    @PutMapping(value = "/{orderId}/payment")
    @Operation(summary = "Update order payment", description = "Update an payment for the specified order")
    public ResponseEntity<OrderDto> updateDeliveryAddress(@PathVariable UUID orderId, @RequestBody UpdatePaymentInput input) throws Exception {
        Order order = orderService.updatePaymentMethod(orderId, input);
        return ResponseEntity.ok(OrderDto.toDto(order));
    }

    @PutMapping(value = "/{orderId}/process")
    @Operation(summary = "Process order", description = "Process an specified order")
    public ResponseEntity<OrderDto> process(@PathVariable UUID orderId) throws Exception {
        Order order = orderService.process(orderId);
        return ResponseEntity.ok(OrderDto.toDto(order));
    }

    @PutMapping(value = "/{orderId}/delivered")
    @Operation(summary = "Delivered order", description = "Delivered an specified order")
    public ResponseEntity<Void> updateDelivered(@PathVariable UUID orderId) throws Exception {
        // TODO IMPLEMENT
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID", description = "Retrieve order details by ID")
    public ResponseEntity<OrderDto> findById(@PathVariable UUID orderId) throws Exception {
        Order order = orderService.findById(orderId);
        return ResponseEntity.ok(OrderDto.toDto(order));
    }

    @GetMapping("/by-customer/{customerId}")
    @Operation(summary = "Get all order by customer", description = "Retrieve all order for the specified customer")
    public ResponseEntity<List<OrderDto>> findAllByCustomerId(@PathVariable long customerId) throws Exception {
        List<Order> order = orderService.findAllByCustomerId(customerId);
        return ResponseEntity.ok(OrderDto.toListDto(order));
    }

}
