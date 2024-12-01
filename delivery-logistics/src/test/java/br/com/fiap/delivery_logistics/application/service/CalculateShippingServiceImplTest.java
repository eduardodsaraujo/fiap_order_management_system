package br.com.fiap.delivery_logistics.application.service;

import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingRequestDto;
import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingResponseDto;
import br.com.fiap.delivery_logistics.application.service.impl.CalculateShippingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

class CalculateShippingServiceImplTest {

    @InjectMocks
    private CalculateShippingServiceImpl calculateShippingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Configuração dos valores simulados para os @Value
        calculateShippingService.setOriginZip("12345-678");
        calculateShippingService.setWeightRate(10.0);
        calculateShippingService.setBaseDeliveryTime(2);
        calculateShippingService.setDistanceRate(1.5);
    }

    @Test
    void shouldCalculateShippingCostAndTime() {
        // Arrange
        var requestDto = new CalculateShippingRequestDto();
        requestDto.setDestinationZipCode("45678-123");
        requestDto.setWeightProducts(5);

        // Act
        var response = calculateShippingService.calculateShipping(requestDto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getDeliveryTime()).isGreaterThanOrEqualTo(2);
        assertThat(response.getCost()).isPositive();
    }

    @Test
    void shouldEstimateDeliveryTimeBasedOnRegion() {
        // Arrange
        var requestDto = new CalculateShippingRequestDto();
        requestDto.setDestinationZipCode("24567123"); // Região 2
        requestDto.setWeightProducts(5);

        // Act
        var response = calculateShippingService.calculateShipping(requestDto);

        // Assert
        assertThat(response.getDeliveryTime()).isEqualTo(4); // baseDeliveryTime (2) + região (2)
    }

    @Test
    void shouldReturnBaseDeliveryTimeForUnexpectedRegion() {
        // Arrange
        var requestDto = new CalculateShippingRequestDto();
        requestDto.setDestinationZipCode("04567123"); // Região 0
        requestDto.setWeightProducts(5);

        // Act
        var response = calculateShippingService.calculateShipping(requestDto);

        // Assert
        assertThat(response.getDeliveryTime()).isEqualTo(2); // Apenas baseDeliveryTime
    }

    @Test
    void shouldCalculateShippingCostWithCorrectFormula() {
        // Arrange
        var requestDto = new CalculateShippingRequestDto();
        requestDto.setDestinationZipCode("34567-123");
        requestDto.setWeightProducts(3); // Peso

        // Act
        var response = calculateShippingService.calculateShipping(requestDto);

        // Assert
        var expectedShippingCost = (3 * 10.0) + (5 * 1.5); // Fórmula: peso * weightRate + tempo * distanceRate
        assertThat(response.getCost()).isEqualTo(expectedShippingCost);
    }
}
