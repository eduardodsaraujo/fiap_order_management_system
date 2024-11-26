package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.mapper.OrderOutputMapper;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.OrderStatus;
import br.com.fiap.order_management.domain.output.OrderOutput;
import br.com.fiap.order_management.util.OrderOutputTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FindAllOrdersByCustomerIdUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private OrderOutputMapper orderOutputMapper;

    @InjectMocks
    private FindAllOrdersByCustomerIdUseCase findAllOrdersByCustomerIdUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ShouldReturnListOfOrderOutputs() {
        Order order = Order.builder()
                .id(java.util.UUID.randomUUID())
                .orderDate(java.time.LocalDate.now())
                .status(OrderStatus.NEW)
                .build();

        when(orderGateway.findAllByCustomerId(anyLong())).thenReturn(Collections.singletonList(order));

        OrderOutput orderOutput = OrderOutputTestUtil.createOrderOutput();
        when(orderOutputMapper.toOrderOutput(order)).thenReturn(orderOutput);

        List<OrderOutput> orderOutputs = findAllOrdersByCustomerIdUseCase.findAll(1L);

        assertNotNull(orderOutputs);
        assertEquals(1, orderOutputs.size());
    }
}
