package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.CustomerGateway;
import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.gateway.ProductGateway;
import br.com.fiap.order_management.domain.input.CreateOrderInput;
import br.com.fiap.order_management.domain.model.Customer;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.Product;
import br.com.fiap.order_management.domain.output.OrderOutput;
import br.com.fiap.order_management.infra.exception.OrderException;
import br.com.fiap.order_management.util.CustomerHelper;
import br.com.fiap.order_management.util.OrderHelper;
import br.com.fiap.order_management.util.ProductHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateOrderUseCaseTest {

    private CreateOrderUseCase createOrderUseCase;

    @Mock
    private OrderGateway orderGateway;
    @Mock
    private CustomerGateway customerGateway;
    @Mock
    private ProductGateway productGateway;
    AutoCloseable openMocks;


    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        createOrderUseCase = new CreateOrderUseCase(orderGateway, customerGateway, productGateway);
    }

    @Test
    public void shouldCreateOrder() {
        // Arrange
        Customer customer = CustomerHelper.createCustomer();
        Product product = ProductHelper.createProduct();
        Order order = OrderHelper.createOrder();

        CreateOrderInput input = OrderHelper.createOrderInput();

        when(customerGateway.findById(anyLong())).thenReturn(customer);
        when(productGateway.findAllByIds(List.of(1L))).thenReturn(List.of(product));
        when(orderGateway.save(any(Order.class))).thenReturn(order);

        // Act
        OrderOutput savedOrder = createOrderUseCase.execute(input);

        // Assert
        verify(customerGateway, times(1)).findById(any(Long.class));
        verify(productGateway, times(1)).findAllByIds(anyList());
        verify(orderGateway, times(1)).save(any(Order.class));

        assertThat(savedOrder).isInstanceOf(OrderOutput.class).isNotNull();
        assertThat(savedOrder.getId()).isEqualTo(order.getId());
        assertThat(savedOrder.getStatus()).isEqualTo(order.getStatus());
        assertThat(savedOrder.getItemTotal()).isEqualTo(order.getItemTotal());
        assertThat(savedOrder.getShippingValue()).isEqualTo(order.getShippingValue());
        assertThat(savedOrder.getTotal()).isEqualTo(order.getTotal());
        assertThat(savedOrder.getTotalWeight()).isEqualTo(order.getTotalWeight());
        assertThat(savedOrder.getCustomer().getId()).isEqualTo(order.getCustomer().getId());
        assertThat(savedOrder.getDeliveryAddress()).isEqualTo(order.getDeliveryAddress());
        assertThat(savedOrder.getPayment()).isEqualTo(order.getPayment());
        assertThat(savedOrder.getItems()).hasSize(1);
    }

    @Test
    public void shouldThrowException_WhenCreateOrder_CustomerNotFound() {
        when(customerGateway.findById(anyLong())).thenThrow(new OrderException("Customer not found"));

        CreateOrderInput createOrderInput = OrderHelper.createOrderInput();

        // Act
        // Assert
        assertThatThrownBy(
                () -> createOrderUseCase.execute(createOrderInput))
                .isInstanceOf(Exception.class)
                .hasMessage("Customer not found");
    }

    @Test
    void shouldThrowException_WhenExecuteOrder_ProductNotFound() {
        Customer customer = CustomerHelper.createCustomer();

        when(customerGateway.findById(anyLong())).thenReturn(customer);
        when(productGateway.findAllByIds(List.of(anyLong()))).thenReturn(new ArrayList<>());

        CreateOrderInput createOrderInput = OrderHelper.createOrderInput();

        // Act
        // Assert
        assertThatThrownBy(
                () -> createOrderUseCase.execute(createOrderInput))
                .isInstanceOf(Exception.class)
                .hasMessage("Product not found");
    }

    @Test
    public void shouldThrowException_WhenCreateOrder_ItemsNotFound() {
        Customer customer = CustomerHelper.createCustomer();
        when(customerGateway.findById(anyLong())).thenReturn(customer);

        CreateOrderInput createOrderInput = OrderHelper.createOrderInput();
        createOrderInput.setItems(new ArrayList<>());

        // Act
        // Assert
        assertThatThrownBy(
                () -> createOrderUseCase.execute(createOrderInput))
                .isInstanceOf(Exception.class)
                .hasMessage("The order does not contain items.");
    }

}