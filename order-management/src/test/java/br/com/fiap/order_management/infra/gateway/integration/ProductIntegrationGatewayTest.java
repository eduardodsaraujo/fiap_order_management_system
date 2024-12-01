package br.com.fiap.order_management.infra.gateway.integration;

import br.com.fiap.order_management.domain.model.Product;
import br.com.fiap.order_management.infra.gateway.integration.dto.product.ProductDto;
import br.com.fiap.order_management.infra.gateway.integration.gateway.ProductWsGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class ProductIntegrationGatewayTest {

    private ProductIntegrationGateway productIntegrationGateway;

    @Mock
    private ProductWsGateway productWsGateway;

    private AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        productIntegrationGateway = new ProductIntegrationGateway(productWsGateway);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (openMocks != null) {
            openMocks.close();
        }
    }

    @Test
    public void shouldFindAllProductsByIds() {
        // Act
        long productId1 = 1L;
        long productId2 = 2L;
        ProductDto productDto1 = new ProductDto();
        productDto1.setId(productId1);
        ProductDto productDto2 = new ProductDto();
        productDto2.setId(productId2);

        when(productWsGateway.findAllByIds(anyList())).thenReturn(Arrays.asList(productDto1, productDto2));

        // Arrange
        List<Product> products = productIntegrationGateway.findAllByIds(Arrays.asList(productId1, productId2));

        // Assert
        verify(productWsGateway, times(1)).findAllByIds(anyList());
        assertThat(products).hasSize(2);
    }

}