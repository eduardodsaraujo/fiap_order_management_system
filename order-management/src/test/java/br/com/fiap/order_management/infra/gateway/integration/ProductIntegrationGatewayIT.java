package br.com.fiap.order_management.infra.gateway.integration;

import br.com.fiap.order_management.domain.model.Product;
import br.com.fiap.order_management.infra.gateway.integration.dto.product.ProductDto;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static br.com.fiap.order_management.util.JsonHelper.asJsonString;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@WireMockTest(httpPort = 8080)
public class ProductIntegrationGatewayIT {

    @Autowired
    private ProductIntegrationGateway productIntegrationGateway;

    @Test
    public void shouldFindAllProductsByIds() {
        // Act
        long productId1 = 1L;
        long productId2 = 2L;
        ProductDto productDto1 = new ProductDto();
        productDto1.setId(productId1);
        ProductDto productDto2 = new ProductDto();
        productDto2.setId(productId2);

        stubFor(get("/product-management/api/products/all/%5B1%2C%202%5D")
                .willReturn(okJson(asJsonString(Arrays.asList(productDto1, productDto2)))));

        // Arrange
        List<Product> products = productIntegrationGateway.findAllByIds(Arrays.asList(productId1, productId2));

        // Assert
        assertThat(products).hasSize(2);
    }

}