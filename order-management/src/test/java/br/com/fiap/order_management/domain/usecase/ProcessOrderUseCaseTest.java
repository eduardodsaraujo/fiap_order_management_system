package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.DeliveryGateway;
import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.gateway.ProductGateway;
import br.com.fiap.order_management.domain.mapper.OrderOutputMapper;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.OrderItem;
import br.com.fiap.order_management.domain.model.Product;
import br.com.fiap.order_management.domain.output.OrderOutput;
import br.com.fiap.order_management.util.OrderOutputTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class ProcessOrderUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private ProductGateway productGateway;

    @Mock
    private DeliveryGateway deliveryGateway;

    @InjectMocks
    private ProcessOrderUseCase processOrderUseCase;

    private UUID orderId;
    private Order order;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();

        Product product = Product.builder()
                .id(1L)
                .code("P123")
                .description("Test Product")
                .price(100.0)
                .weight(1.5)
                .build();

        order = Order.builder()
                .id(orderId)
                .items(Collections.singletonList(new OrderItem(UUID.randomUUID(), 2, product)))
                .build();
    }

    @Test
    void process_ShouldProcessOrderSuccessfully() {
        when(orderGateway.findById(orderId)).thenReturn(order);

        doNothing().when(productGateway).decreaseStock(anyLong(), anyDouble());

        doNothing().when(deliveryGateway).createDelivery(any(), any());

        OrderOutput expectedOrderOutput = OrderOutputTestUtil.createOrderOutput();
        when(OrderOutputMapper.toOrderOutput(order)).thenReturn(expectedOrderOutput);

        OrderOutput result = processOrderUseCase.process(orderId);

        verify(orderGateway, times(1)).findById(orderId);
        verify(productGateway, times(1)).decreaseStock(anyLong(), anyDouble());
        verify(deliveryGateway, times(1)).createDelivery(any(), any());
        verify(orderGateway, times(1)).save(order);

        assertNotNull(result);
        assertEquals(expectedOrderOutput, result);
    }

    @Test
    void process_ShouldHandleExceptionAndRevertStock() {
        when(orderGateway.findById(orderId)).thenReturn(order);

        doNothing().when(productGateway).decreaseStock(anyLong(), anyDouble());

        doThrow(new RuntimeException("Delivery failed")).when(deliveryGateway).createDelivery(any(), any());

        OrderOutput result = processOrderUseCase.process(orderId);

        verify(orderGateway, times(1)).findById(orderId);
        verify(productGateway, times(1)).decreaseStock(anyLong(), anyDouble());
        verify(deliveryGateway, times(1)).createDelivery(any(), any());
        verify(productGateway, times(1)).increaseStock(anyLong(), anyDouble());

        assertNotNull(result);
    }
}
