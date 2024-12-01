package br.com.fiap.delivery_logistics.controller;

import br.com.fiap.delivery_logistics.api.controller.DeliveryController;
import br.com.fiap.delivery_logistics.application.dto.delivery.*;
import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingRequestDto;
import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingResponseDto;
import br.com.fiap.delivery_logistics.application.service.CalculateShippingService;
import br.com.fiap.delivery_logistics.application.service.DeliveryService;
import br.com.fiap.delivery_logistics.domain.model.DeliveryStatus;
import br.com.fiap.delivery_logistics.infrastructure.exception.GlobalExceptionHandler;
import br.com.fiap.delivery_logistics.utils.DeliveryHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static br.com.fiap.delivery_logistics.utils.DeliveryHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DeliveryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DeliveryService deliveryService;

    @Mock
    private CalculateShippingService calculateShippingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        DeliveryController deliveryController = new DeliveryController(deliveryService, calculateShippingService);
        mockMvc = MockMvcBuilders.standaloneSetup(deliveryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }


    @Test
    void shouldCreateDelivery() throws Exception {
        // Arrange
        DeliveryRequestDto requestDto = createDeliveryRequestDto();
        DeliveryResponseDto responseDto = createDeliveryResponseDto(DeliveryStatus.PENDING);

        when(deliveryService.createDelivery(any(DeliveryRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/delivery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(responseDto.getOrderId().toString()));

        verify(deliveryService, times(1)).createDelivery(any(DeliveryRequestDto.class));
    }

    @Test
    void shouldUpdateDeliveryTrack() throws Exception {
        // Arrange
        DeliveryTrackRequestDto requestDto = createDeliveryTrackRequestDto();
        DeliveryResponseDto responseDto = createDeliveryResponseDto(DeliveryStatus.ON_THE_WAY); // Populate as needed

        when(deliveryService.saveDeliveryTrack(any(UUID.class), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(put("/api/delivery/update-track")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(responseDto.getOrderId().toString())); // Adjust with real fields

        verify(deliveryService, times(1)).saveDeliveryTrack(any(UUID.class), any(BigDecimal.class), any(BigDecimal.class));
    }

    @Test
    void shouldGetDeliveryById() throws Exception {
        // Arrange
        UUID id = UUID.fromString("916d6109-ec12-4220-b331-a7f06e62a4ee");
        DeliveryResponseDto responseDto = createDeliveryResponseDto(DeliveryStatus.PENDING); // Populate as needed

        when(deliveryService.getById(id)).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(get("/api/delivery/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(responseDto.getOrderId().toString()));

        verify(deliveryService, times(1)).getById(id);
    }

    @Test
    void shouldCalculateShipping() throws Exception {
        // Arrange
        CalculateShippingRequestDto requestDto = createCalculateShippingRequestDto();
        CalculateShippingResponseDto responseDto = createCalculateShippingResponseDto();

        when(calculateShippingService.calculateShipping(any(CalculateShippingRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/delivery/calculate-shipping")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cost").value(responseDto.getCost()));

        verify(calculateShippingService, times(1)).calculateShipping(any(CalculateShippingRequestDto.class));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
