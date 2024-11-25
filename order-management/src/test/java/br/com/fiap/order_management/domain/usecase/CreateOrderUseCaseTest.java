package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.CustomerGateway;
import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.gateway.ProductGateway;
import br.com.fiap.order_management.domain.input.CreateOrderInput;
import br.com.fiap.order_management.domain.input.OrderItemInput;
import br.com.fiap.order_management.domain.mapper.OrderOutputMapper;
import br.com.fiap.order_management.domain.model.Customer;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.OrderItem;
import br.com.fiap.order_management.domain.model.Product;
import br.com.fiap.order_management.domain.output.OrderOutput;
import br.com.fiap.order_management.infra.exception.OrderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateOrderUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private CustomerGateway customerGateway;

    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_ShouldReturnOrderOutput_WhenValidInput() {
        CreateOrderInput input = CreateOrderInput.builder()
                .customerId(1L)
                .items(List.of(OrderItemInput.builder()
                        .productId(1L)
                        .quantity(2)
                        .build()))
                .build();

        Customer mockCustomer = Customer.builder().id(1L).name("Test Customer").build();
        Product mockProduct = Product.builder().id(1L).description("Test Product").price(100.0).build();
        Order mockOrder = Order.builder().id(UUID.randomUUID()).customer(mockCustomer).items(List.of(
                OrderItem.builder().id(UUID.randomUUID()).quantity(2).product(mockProduct).build()
        )).build();
        OrderOutput mockOutput = OrderOutputMapper.toOrderOutput(mockOrder);

        when(customerGateway.findById(1L)).thenReturn(mockCustomer);
        when(productGateway.findAllByIds(List.of(1L))).thenReturn(List.of(mockProduct));
        when(orderGateway.save(any(Order.class))).thenReturn(mockOrder);

        OrderOutput result = createOrderUseCase.create(input);

        assertNotNull(result);
        assertEquals(mockOutput.getId(), result.getId());
        assertEquals(mockOutput.getCustomer().getId(), result.getCustomer().getId());

        verify(customerGateway).findById(1L);
        verify(productGateway).findAllByIds(List.of(1L));
        verify(orderGateway).save(any(Order.class));
    }

    @Test
    void create_ShouldThrowException_WhenCustomerNotFound() {
        CreateOrderInput input = CreateOrderInput.builder().customerId(999L).build();

        when(customerGateway.findById(999L)).thenThrow(new OrderException("Customer not found"));

        OrderException exception = assertThrows(OrderException.class, () -> createOrderUseCase.create(input));
        assertEquals("Customer not found", exception.getMessage());

        verify(customerGateway).findById(999L);
        verifyNoInteractions(productGateway, orderGateway);
    }

    @Test
    void create_ShouldThrowException_WhenProductNotFound() {
        CreateOrderInput input = CreateOrderInput.builder()
                .customerId(1L)
                .items(List.of(OrderItemInput.builder().productId(99L).quantity(1).build()))
                .build();

        Customer mockCustomer = Customer.builder().id(1L).name("Test Customer").build();
        when(customerGateway.findById(1L)).thenReturn(mockCustomer);
        when(productGateway.findAllByIds(List.of(99L))).thenReturn(List.of());

        OrderException exception = assertThrows(OrderException.class, () -> createOrderUseCase.create(input));
        assertEquals("Order not found", exception.getMessage());

        verify(customerGateway).findById(1L);
        verify(productGateway).findAllByIds(List.of(99L));
        verifyNoInteractions(orderGateway);
    }
}