package br.com.fiap.delivery_logistics.controller;

import br.com.fiap.delivery_logistics.api.controller.DeliveryController;
import br.com.fiap.delivery_logistics.application.dto.delivery.*;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonResponseDto;
import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingRequestDto;
import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingResponseDto;
import br.com.fiap.delivery_logistics.application.service.CalculateShippingService;
import br.com.fiap.delivery_logistics.application.service.DeliveryService;
import br.com.fiap.delivery_logistics.domain.model.DeliveryStatus;
import br.com.fiap.delivery_logistics.infrastructure.exception.DeliveryException;
import br.com.fiap.delivery_logistics.infrastructure.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
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

    @Test
    void shouldGetAllDeliveries() throws Exception {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        var delivery = createDeliveryResponseDto(DeliveryStatus.PENDING);
        var delivery2 = createDeliveryResponseDto(DeliveryStatus.PENDING);

        List<DeliveryResponseDto> deliveryList = Arrays.asList(delivery, delivery2);

        Page<DeliveryResponseDto> deliveryPage =
                new PageImpl<>(deliveryList, pageable, deliveryList.size());

        when(deliveryService.findAllDeliveries(any(PageRequest.class))).thenReturn(deliveryPage);

        // Act & Assert
        mockMvc.perform(get("/api/delivery")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].orderId").value(deliveryPage.getContent().get(0).getOrderId().toString()));

        verify(deliveryService, times(1)).findAllDeliveries(any(Pageable.class));
    }

    @Test
    void shouldUpdateDeliveryStatus() throws Exception {
        // Arrange
        UUID deliveryId = UUID.randomUUID();
        ChangeDeliveryStatusRequestDto requestDto = new ChangeDeliveryStatusRequestDto(deliveryId, DeliveryStatus.PENDING); // Example payload
        DeliveryResponseDto responseDto = createDeliveryResponseDto(DeliveryStatus.DELIVERED);

        when(deliveryService.changeDeliveryStatus(eq(deliveryId), any(ChangeDeliveryStatusRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(put("/api/delivery/{id}", deliveryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(responseDto.getOrderId().toString()))
                .andExpect(jsonPath("$.status").value(responseDto.getStatus().toString()));

        verify(deliveryService, times(1)).changeDeliveryStatus(eq(deliveryId), any(ChangeDeliveryStatusRequestDto.class));
    }

    @Test
    void shouldReturnBadRequestWhenOrderIdNotProvided() throws Exception {
        // Arrange
        DeliveryTrackRequestDto requestDto = new DeliveryTrackRequestDto(null, new BigDecimal("40.7128"), new BigDecimal("-74.0060"));

        // Act & Assert
        mockMvc.perform(put("/api/delivery/update-track")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Validation error"));

        verify(deliveryService, never()).saveDeliveryTrack(any(), any(), any());
    }

    @Test
    void shouldReturnNotFoundWhenOrderIdDoesNotExist() throws Exception {
        // Arrange
        UUID nonExistentOrderId = UUID.randomUUID();

        when(deliveryService.getById(nonExistentOrderId))
                .thenThrow(new DeliveryException("Delivery not found."));

        // Act & Assert
        mockMvc.perform(get("/api/delivery/{id}", nonExistentOrderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message").value("Delivery not found."));

        verify(deliveryService, times(1)).getById(nonExistentOrderId);
    }


}
