package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.AddressGateway;
import br.com.fiap.order_management.domain.gateway.DeliveryGateway;
import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.input.UpdateDeliveryAddressInput;
import br.com.fiap.order_management.domain.mapper.OrderOutputMapper;
import br.com.fiap.order_management.domain.model.DeliveryAddress;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.output.OrderOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UpdateOrderDeliveryAddressUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private AddressGateway addressGateway;

    @Mock
    private DeliveryGateway deliveryGateway;

    @InjectMocks
    private UpdateOrderDeliveryAddressUseCase updateOrderDeliveryAddressUseCase;

    private UUID orderId;
    private Order order;
    private DeliveryAddress deliveryAddress;
    private UpdateDeliveryAddressInput input;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        order = mock(Order.class);
        deliveryAddress = mock(DeliveryAddress.class);
        input = UpdateDeliveryAddressInput.builder()
                .deliveryAddressId(1L)
                .build();

        // Mockando comportamentos
        when(orderGateway.findById(orderId)).thenReturn(order);
        when(addressGateway.findAddressByCustomerIdAndAddressId(anyLong(), anyLong())).thenReturn(deliveryAddress);
        when(deliveryGateway.calculateShipping(any(), anyDouble())).thenReturn(10.0);
    }

    @Test
    void update_ShouldUpdateOrderWithNewDeliveryAddressAndShipping() {
        OrderOutput orderOutput = updateOrderDeliveryAddressUseCase.update(orderId, input);
        verify(addressGateway, times(1)).findAddressByCustomerIdAndAddressId(anyLong(), anyLong());
        verify(deliveryGateway, times(1)).calculateShipping(any(), anyDouble());
        verify(order, times(1)).updateDeliveryAddress(any(DeliveryAddress.class), eq(10.0));
        verify(orderGateway, times(1)).save(order);
        assertNotNull(orderOutput);
    }
}
