package br.com.fiap.order_management.controller;

import br.com.fiap.order_management.api.controller.OrderController;
import br.com.fiap.order_management.domain.input.CreateOrderInput;
import br.com.fiap.order_management.domain.input.OrderItemInput;
import br.com.fiap.order_management.domain.input.UpdateDeliveryAddressInput;
import br.com.fiap.order_management.domain.input.UpdatePaymentInput;
import br.com.fiap.order_management.domain.output.OrderOutput;
import br.com.fiap.order_management.domain.usecase.*;
import br.com.fiap.order_management.util.OrderOutputTestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CreateOrderUseCase createOrderUseCase;

    @Mock
    private UpdateOrderDeliveryAddressUseCase updateOrderDeliveryAddressUseCase;

    @Mock
    private UpdateOrderPaymentMethodUseCase updateOrderPaymentMethodUseCase;

    @Mock
    private UpdateOrderDeliveredUseCase updateOrderDeliveredUseCase;

    @Mock
    private ProcessOrderUseCase processOrderUseCase;

    @Mock
    private FindOrderByIdUseCase findOrderByIdUseCase;

    @Mock
    private FindAllOrdersByCustomerIdUseCase findAllOrdersByCustomerIdUseCase;

    @InjectMocks
    private OrderController orderController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_ShouldReturnOrderOutput() throws Exception {
        CreateOrderInput input = CreateOrderInput.builder()
                .customerId(1L)
                .items(List.of(OrderItemInput.builder()
                        .productId(1L)
                        .quantity(2)
                        .build()))
                .build();
        input.setCustomerId(1L);

        OrderOutput output = OrderOutputTestUtil.createOrderOutput();

        when(createOrderUseCase.create(any(CreateOrderInput.class))).thenReturn(output);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.customer.id").value(output.getCustomer().getId()));
    }

    @Test
    void updateDeliveryAddress_ShouldReturnOrderOutput() throws Exception {
        UUID orderId = UUID.randomUUID();
        UpdateDeliveryAddressInput input = UpdateDeliveryAddressInput.builder()
                .deliveryAddressId(1L)
                .build();

        OrderOutput output = OrderOutputTestUtil.createOrderOutput();

        when(updateOrderDeliveryAddressUseCase.update(any(UUID.class), any(UpdateDeliveryAddressInput.class)))
                .thenReturn(output);

        mockMvc.perform(put("/api/orders/" + orderId + "/delivery-address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(output.getId().toString()))
                .andExpect(jsonPath("$.deliveryAddress.street").value(output.getDeliveryAddress().getStreet()));
    }

    @Test
    void findById_ShouldReturnOrderOutput() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderOutput output = OrderOutputTestUtil.createOrderOutput();

        when(findOrderByIdUseCase.findById(orderId)).thenReturn(output);

        mockMvc.perform(get("/api/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId.toString()));
    }

    @Test
    void findAllByCustomerId_ShouldReturnOrderList() throws Exception {
        long customerId = 1L;
        OrderOutput output = OrderOutputTestUtil.createOrderOutput();

        when(findAllOrdersByCustomerIdUseCase.findAll(customerId))
                .thenReturn(Collections.singletonList(output));

        mockMvc.perform(get("/api/orders/by-customer/" + customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].customer.id").value(output.getCustomer().getId()));
    }
}
