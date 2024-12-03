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
import br.com.fiap.order_management.infra.gateway.integration.dto.customer.CustomerDto;
import br.com.fiap.order_management.infra.gateway.integration.dto.product.ProductDto;
import br.com.fiap.order_management.util.CustomerHelper;
import br.com.fiap.order_management.util.JsonHelper;
import br.com.fiap.order_management.util.OrderHelper;
import br.com.fiap.order_management.util.ProductHelper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static br.com.fiap.order_management.api.controller.OrderControllerTest.asJsonString;
import static br.com.fiap.order_management.domain.model.OrderStatus.OPEN;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WireMockTest(httpPort = 8080)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
public class CreateOrderUseCaseIT {

    @Autowired
    private CreateOrderUseCase createOrderUseCase;

    @Test
    public void shouldCreateOrder() throws InterruptedException {
        // Arrange
        CustomerDto customerDto = CustomerHelper.createCustomerDto();
        stubFor(get("/customer-management/api/customers/1")
                .willReturn(okJson(asJsonString(customerDto))));

        long productId1 = 1L;
        ProductDto productDto1 = ProductHelper.createProductDto();
        productDto1.setId(productId1);
        stubFor(get("/product-management/api/products/all/%5B1%5D")
                .willReturn(okJson(JsonHelper.asJsonString(Arrays.asList(productDto1)))));

        CreateOrderInput input = OrderHelper.createOrderInput();

        // Act
        OrderOutput savedOrder = createOrderUseCase.execute(input);

        // Assert
        assertThat(savedOrder).isInstanceOf(OrderOutput.class).isNotNull();
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getStatus()).isEqualTo(OPEN);
        assertThat(savedOrder.getItemTotal()).isEqualTo(16000.0);
        assertThat(savedOrder.getShippingValue()).isEqualTo(0.0);
        assertThat(savedOrder.getTotal()).isEqualTo(16000.0);
        assertThat(savedOrder.getTotalWeight()).isEqualTo(2.0);
        assertThat(savedOrder.getCustomer().getId()).isEqualTo(input.getCustomerId());
        assertThat(savedOrder.getItems()).hasSize(1);
    }

}