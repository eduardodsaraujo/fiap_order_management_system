package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.mapper.OrderOutputMapper;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.output.OrderOutput;
import br.com.fiap.order_management.util.OrderOutputTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FindOrderByIdUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private OrderOutputMapper orderOutputMapper;

    @InjectMocks
    private FindOrderByIdUseCase findOrderByIdUseCase;

    private UUID orderId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
    }

    @Test
    void findById_ShouldReturnOrderOutput() {
        Order order = Order.builder()
                .id(orderId)
                .orderDate(java.time.LocalDate.now())
                .status(br.com.fiap.order_management.domain.model.OrderStatus.NEW)
                .build();

        when(orderGateway.findById(orderId)).thenReturn(order);

        OrderOutput expectedOrderOutput = OrderOutputTestUtil.createOrderOutput();
        when(orderOutputMapper.toOrderOutput(order)).thenReturn(expectedOrderOutput);

        OrderOutput orderOutput = findOrderByIdUseCase.findById(orderId);

        assertNotNull(orderOutput);
        assertEquals(expectedOrderOutput, orderOutput);
    }
}
