package br.com.fiap.order_management.api.controller;

import br.com.fiap.order_management.domain.input.CreateOrderInput;
import br.com.fiap.order_management.domain.input.PaymentInput;
import br.com.fiap.order_management.domain.input.UpdateDeliveryAddressInput;
import br.com.fiap.order_management.domain.model.PaymentMethod;
import br.com.fiap.order_management.domain.output.OrderOutput;
import br.com.fiap.order_management.domain.usecase.*;
import br.com.fiap.order_management.infra.exception.GlobalExceptionHandler;
import br.com.fiap.order_management.util.OrderHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateOrderUseCase createOrderUseCase;
    @Mock
    private UpdateOrderDeliveryAddressUseCase updateOrderDeliveryAddressUseCase;
    @Mock
    private OrderCheckoutUseCase orderCheckoutUseCase;
    @Mock
    private UpdateOrderDeliveredUseCase updateOrderDeliveredUseCase;
    @Mock
    private FindOrderByIdUseCase findOrderByIdUseCase;
    @Mock
    private FindAllOrdersByCustomerIdUseCase findAllOrdersByCustomerIdUseCase;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        OrderController orderController = new OrderController(
                createOrderUseCase, updateOrderDeliveryAddressUseCase, orderCheckoutUseCase,
                updateOrderDeliveredUseCase, findOrderByIdUseCase, findAllOrdersByCustomerIdUseCase);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setMessageConverters(mappingJackson2HttpMessageConverter)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        openMocks.close();
    }


    @Test
    public void shouldCreateOrder() throws Exception {
        OrderOutput orderOutput = OrderHelper.createOrderOutput();

        CreateOrderInput input = OrderHelper.createOrderInput();

        when(createOrderUseCase.execute(any(CreateOrderInput.class))).thenReturn(orderOutput);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderOutput.getId().toString()))
                .andExpect(jsonPath("$.orderDate").value(orderOutput.getOrderDate().toString()))
                .andExpect(jsonPath("$.status").value(orderOutput.getStatus().toString()))
                .andExpect(jsonPath("$.itemTotal").value(orderOutput.getItemTotal()))
                .andExpect(jsonPath("$.shippingValue").value(orderOutput.getShippingValue()))
                .andExpect(jsonPath("$.total").value(orderOutput.getTotal()))
                .andExpect(jsonPath("$.totalWeight").value(orderOutput.getTotalWeight()))
                .andExpect(jsonPath("$.customer.id").value(orderOutput.getCustomer().getId()));

        verify(createOrderUseCase, times(1)).execute(any(CreateOrderInput.class));
    }

    @Test
    public void shouldUpdateDeliveryAddress() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderOutput orderOutput = OrderHelper.createOrderOutput();
        orderOutput.setId(orderId);

        UpdateDeliveryAddressInput input = new UpdateDeliveryAddressInput(1L);

        when(updateOrderDeliveryAddressUseCase.execute(any(UUID.class), any(UpdateDeliveryAddressInput.class))).thenReturn(orderOutput);

        mockMvc.perform(put("/api/orders/{orderId}/delivery-address", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderOutput.getId().toString()))
                .andExpect(jsonPath("$.orderDate").value(orderOutput.getOrderDate().toString()))
                .andExpect(jsonPath("$.status").value(orderOutput.getStatus().toString()))
                .andExpect(jsonPath("$.itemTotal").value(orderOutput.getItemTotal()))
                .andExpect(jsonPath("$.shippingValue").value(orderOutput.getShippingValue()))
                .andExpect(jsonPath("$.total").value(orderOutput.getTotal()))
                .andExpect(jsonPath("$.totalWeight").value(orderOutput.getTotalWeight()))
                .andExpect(jsonPath("$.customer.id").value(orderOutput.getCustomer().getId()));

        verify(updateOrderDeliveryAddressUseCase, times(1)).execute(any(UUID.class), any(UpdateDeliveryAddressInput.class));
    }

    @Test
    public void shouldCheckoutOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderOutput orderOutput = OrderHelper.createOrderOutput();
        orderOutput.setId(orderId);

        PaymentInput input = new PaymentInput(PaymentMethod.PIX);

        when(orderCheckoutUseCase.execute(any(UUID.class), any(PaymentInput.class))).thenReturn(orderOutput);

        mockMvc.perform(put("/api/orders/{orderId}/checkout", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderOutput.getId().toString()))
                .andExpect(jsonPath("$.orderDate").value(orderOutput.getOrderDate().toString()))
                .andExpect(jsonPath("$.status").value(orderOutput.getStatus().toString()))
                .andExpect(jsonPath("$.itemTotal").value(orderOutput.getItemTotal()))
                .andExpect(jsonPath("$.shippingValue").value(orderOutput.getShippingValue()))
                .andExpect(jsonPath("$.total").value(orderOutput.getTotal()))
                .andExpect(jsonPath("$.totalWeight").value(orderOutput.getTotalWeight()))
                .andExpect(jsonPath("$.customer.id").value(orderOutput.getCustomer().getId()));

        verify(orderCheckoutUseCase, times(1)).execute(any(UUID.class), any(PaymentInput.class));
    }

    @Test
    public void shouldUpdateDelivered() throws Exception {
        UUID orderId = UUID.randomUUID();

        doNothing().when(updateOrderDeliveredUseCase).execute(any(UUID.class));

        mockMvc.perform(put("/api/orders/{orderId}/delivered", orderId))
                .andExpect(status().isOk());

        verify(updateOrderDeliveredUseCase, times(1)).execute(any(UUID.class));
    }

    @Test
    public void shouldFindById() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderOutput orderOutput = OrderHelper.createOrderOutput();
        orderOutput.setId(orderId);

        when(findOrderByIdUseCase.execute(orderId)).thenReturn(orderOutput);

        mockMvc.perform(get("/api/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderOutput.getId().toString()))
                .andExpect(jsonPath("$.orderDate").value(orderOutput.getOrderDate().toString()))
                .andExpect(jsonPath("$.status").value(orderOutput.getStatus().toString()))
                .andExpect(jsonPath("$.itemTotal").value(orderOutput.getItemTotal()))
                .andExpect(jsonPath("$.shippingValue").value(orderOutput.getShippingValue()))
                .andExpect(jsonPath("$.total").value(orderOutput.getTotal()))
                .andExpect(jsonPath("$.totalWeight").value(orderOutput.getTotalWeight()))
                .andExpect(jsonPath("$.customer.id").value(orderOutput.getCustomer().getId()));

        verify(findOrderByIdUseCase, times(1)).execute(any(UUID.class));
    }

    @Test
    public void shouldFindAllByCustomerId() throws Exception {
        long customerId = 1L;

        OrderOutput order1 = OrderHelper.createOrderOutput();
        OrderOutput order2 = OrderHelper.createOrderOutput();
        var orders = Arrays.asList(order1, order2);

        when(findAllOrdersByCustomerIdUseCase.execute(customerId)).thenReturn(orders);

        mockMvc.perform(get("/api/orders/by-customer/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(order1.getId().toString()))
                .andExpect(jsonPath("$[0].orderDate").value(order1.getOrderDate().toString()))
                .andExpect(jsonPath("$[0].status").value(order1.getStatus().toString()))
                .andExpect(jsonPath("$[0].itemTotal").value(order1.getItemTotal()))
                .andExpect(jsonPath("$[0].shippingValue").value(order1.getShippingValue()))
                .andExpect(jsonPath("$[0].total").value(order1.getTotal()))
                .andExpect(jsonPath("$[0].totalWeight").value(order1.getTotalWeight()))
                .andExpect(jsonPath("$[0].customer.id").value(order1.getCustomer().getId()));

        verify(findAllOrdersByCustomerIdUseCase, times(1)).execute(anyLong());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
