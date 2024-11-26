package br.com.fiap.delivery_logistics.controller;

import br.com.fiap.delivery_logistics.api.controller.DeliveryController;
import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryRequestDto;
import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryResponseDto;
import br.com.fiap.delivery_logistics.application.dto.delivery.ChangeDeliveryStatusRequestDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonResponseDto;
import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingRequestDto;
import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingResponseDto;
import br.com.fiap.delivery_logistics.application.service.CalculateShippingService;
import br.com.fiap.delivery_logistics.application.service.DeliveryService;
import br.com.fiap.delivery_logistics.domain.model.DeliveryPersonStatus;
import br.com.fiap.delivery_logistics.domain.model.DeliveryStatus;
import br.com.fiap.delivery_logistics.domain.model.VehicleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DeliveryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DeliveryService deliveryService;

    @Mock
    private CalculateShippingService calculateShippingService;

    @InjectMocks
    private DeliveryController deliveryController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(deliveryController).build();
    }

    @Test
    void createDelivery_ShouldReturnDeliveryResponseDto() throws Exception {
        UUID orderId = UUID.randomUUID();
        DeliveryPersonResponseDto deliveryPerson = new DeliveryPersonResponseDto(1L, "John Doe", VehicleType.CAR, DeliveryPersonStatus.AVAILABLE);
        DeliveryResponseDto responseDto = new DeliveryResponseDto(
                orderId,
                DeliveryStatus.PENDING,
                new BigDecimal("10.1234"),
                new BigDecimal("20.1234"),
                LocalDateTime.now(),
                deliveryPerson
        );

        when(deliveryService.createDelivery(any(DeliveryRequestDto.class))).thenReturn(responseDto);
        DeliveryRequestDto requestDto = new DeliveryRequestDto();
        mockMvc.perform(post("/api/delivery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(orderId.toString()))
                .andExpect(jsonPath("$.status").value(DeliveryStatus.PENDING.toString()))
                .andExpect(jsonPath("$.deliveryPerson.nome").value("John Doe"));
    }

    @Test
    void getDelivery_ShouldReturnDeliveryResponseDto() throws Exception {
        UUID deliveryId = UUID.randomUUID();
        DeliveryPersonResponseDto deliveryPerson = new DeliveryPersonResponseDto(1L, "John Doe", VehicleType.CAR, DeliveryPersonStatus.AVAILABLE);
        DeliveryResponseDto responseDto = new DeliveryResponseDto(
                deliveryId,
                DeliveryStatus.PENDING,
                new BigDecimal("10.1234"),
                new BigDecimal("20.1234"),
                LocalDateTime.now(),
                deliveryPerson
        );

        when(deliveryService.getById(any(UUID.class))).thenReturn(responseDto);
        mockMvc.perform(get("/api/delivery/" + deliveryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(deliveryId.toString()))
                .andExpect(jsonPath("$.status").value(DeliveryStatus.PENDING.toString()))
                .andExpect(jsonPath("$.deliveryPerson.nome").value("John Doe"));
    }

    @Test
    void calculateShipping_ShouldReturnShippingCostAndEta() throws Exception {
        CalculateShippingRequestDto requestDto = new CalculateShippingRequestDto();
        CalculateShippingResponseDto responseDto = new CalculateShippingResponseDto(50.0, 2);

        when(calculateShippingService.calculateShipping(any(CalculateShippingRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/delivery/calculate-shipping")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shippingCost").value(50.0))
                .andExpect(jsonPath("$.estimatedTimeOfArrival").value(2));
    }
}
