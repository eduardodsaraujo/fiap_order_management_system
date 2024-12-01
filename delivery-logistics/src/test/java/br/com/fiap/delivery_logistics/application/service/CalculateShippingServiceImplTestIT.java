package br.com.fiap.delivery_logistics.application.service;

import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingRequestDto;
import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingResponseDto;
import br.com.fiap.delivery_logistics.application.service.impl.CalculateShippingServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CalculateShippingServiceImplTestIT {

    @Autowired
    private CalculateShippingServiceImpl calculateShippingService;

    @Value("${originZip}")
    private String originZip;

    @Value("${weightRate}")
    private double weightRate;

    @Value("${baseDeliveryTime}")
    private int baseDeliveryTime;

    @Value("${distanceRate}")
    private double distanceRate;

    private CalculateShippingRequestDto requestDto;

    @BeforeEach
    void setUp() {
        // Configurando os valores iniciais para o DTO de teste
        requestDto = CalculateShippingRequestDto.builder()
                .destinationZipCode("30000000")
                .weightProducts(10)
                .build();
    }

    @Test
    void shouldCalculateShippingCorrectlyForGivenRequest() {
        // Act: Chamando o método de cálculo
        CalculateShippingResponseDto responseDto = calculateShippingService.calculateShipping(requestDto);

        // Assert: Verificando os resultados
        assertNotNull(responseDto);
        assertTrue(responseDto.getCost() > 0, "O custo do frete deve ser maior que zero");
        assertTrue(responseDto.getDeliveryTime() > baseDeliveryTime, "O tempo de entrega deve ser ajustado com base no CEP de destino");

        // Validando o cálculo esperado
        int expectedDeliveryTime = baseDeliveryTime + 3; // Para o CEP começando com '3'
        double expectedShippingCost = (requestDto.getWeightProducts() * weightRate) + (expectedDeliveryTime * distanceRate);

        assertEquals(expectedDeliveryTime, responseDto.getDeliveryTime());
        assertEquals(expectedShippingCost, responseDto.getCost(), 0.01);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void shouldAdjustDeliveryTimeForDifferentRegions() {
        // Testando ajustes de tempo de entrega com base no primeiro dígito do CEP
        requestDto.setDestinationZipCode("60000000"); // CEP iniciando com '6'

        CalculateShippingResponseDto responseDto = calculateShippingService.calculateShipping(requestDto);

        int expectedDeliveryTime = baseDeliveryTime + 6; // Ajuste para '6'
        assertEquals(expectedDeliveryTime, responseDto.getDeliveryTime());
    }

    @Test
    void shouldNotAddExtraTimeForInvalidRegion() {
        // Testando quando o CEP não requer ajustes (ex: iniciando com '0' ou '1')
        requestDto.setDestinationZipCode("10000000");

        CalculateShippingResponseDto responseDto = calculateShippingService.calculateShipping(requestDto);

        assertEquals(baseDeliveryTime, responseDto.getDeliveryTime());
    }
}
