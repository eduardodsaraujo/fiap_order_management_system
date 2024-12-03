package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.infra.gateway.db.mongo.repository.OrderRepository;
import br.com.fiap.order_management.util.OrderHelper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class FindAllOrdersByCustomerIdUseCaseIT {

    @Autowired
    private FindAllOrdersByCustomerIdUseCase findAllOrdersByCustomerIdUseCase;
    @Autowired
    private OrderGateway OrderGateway;

    @Test
    void shouldFindAllOrdersByCustomerId() {
        // Arrange
        long customerId = 1L;

        Order order1 = OrderHelper.createOrder();
        Order order2 = OrderHelper.createOrder();
        OrderGateway.save(order1);
        OrderGateway.save(order2);

        // Act
        var foundOrders = findAllOrdersByCustomerIdUseCase.execute(customerId);

        // Assert
        assertThat(foundOrders.size()).isGreaterThan(1);
    }
}
