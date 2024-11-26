package br.com.fiap.delivery_logistics.application.service;

import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingRequestDto;
import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingResponseDto;
import br.com.fiap.delivery_logistics.application.service.impl.CalculateShippingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CalculateShippingServiceImplTest {

    private CalculateShippingServiceImpl calculateShippingService;

    @Value("${originZip}")
    private String originZip;

    @Value("${weightRate}")
    private double weightRate;

    @Value("${baseDeliveryTime}")
    private int baseDeliveryTime;

    @Value("${distanceRate}")
    private double distanceRate;

    @BeforeEach

    void setUp() {
        calculateShippingService = new CalculateShippingServiceImpl();
        calculateShippingService.setOriginZip("01451-000");
        calculateShippingService.setWeightRate(1.2);
        calculateShippingService.setBaseDeliveryTime(5);
        calculateShippingService.setDistanceRate(0.8);
    }


    @Test
    void calculateShipping_ShouldReturnCorrectShippingCostAndDeliveryTime() {
        CalculateShippingRequestDto requestDto = new CalculateShippingRequestDto("12345678", 10);
        CalculateShippingResponseDto response = calculateShippingService.calculateShipping(requestDto);
        double expectedShippingCost = (10.0 * weightRate) + (5 * distanceRate);
        int expectedDeliveryTime = baseDeliveryTime + 4;
        assertEquals(expectedShippingCost, response.getCost());
        assertEquals(expectedDeliveryTime, response.getDeliveryTime());
    }
}
