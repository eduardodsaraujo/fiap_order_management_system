package br.com.fiap.delivery_logistics.controller;


import br.com.fiap.delivery_logistics.api.controller.DeliveryPersonController;
import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryResponseDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.ChangeDeliveryPersonStatusRequestDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonRequestDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonResponseDto;
import br.com.fiap.delivery_logistics.application.service.DeliveryPersonService;
import br.com.fiap.delivery_logistics.domain.model.DeliveryPersonStatus;
import br.com.fiap.delivery_logistics.domain.model.DeliveryStatus;
import br.com.fiap.delivery_logistics.domain.model.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class DeliveryPersonControllerTest {

    @Mock
    private DeliveryPersonService deliveryPersonService;

    @InjectMocks
    private DeliveryPersonController deliveryPersonController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(deliveryPersonController).build();
    }

    @Test
    void testSaveDeliveryPerson() throws Exception {
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

        when(deliveryPersonService.createDeliveryPerson(any())).thenReturn(responseDto.getDeliveryPerson());
        mockMvc.perform(post("/api/deliveryperson")
                        .contentType("application/json")
                        .content("{\"name\":\"John Doe\",\"status\":\"Available\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.status").value("Available"));

        verify(deliveryPersonService, times(1)).createDeliveryPerson(any());
    }

    @Test
    void testUpdateDeliveryPersonStatus() throws Exception {
        DeliveryPersonResponseDto responseDto = new DeliveryPersonResponseDto(
                1L, "John Doe", VehicleType.CAR, DeliveryPersonStatus.UNAVAILABLE
        );
        when(deliveryPersonService.changeDeliveryPersonStatus(anyLong(), any())).thenReturn(responseDto);

        mockMvc.perform(put("/api/deliveryperson/{id}", 1L)
                        .contentType("application/json")
                        .content("{\"deliveryPersonId\":1,\"status\":\"UNAVAILABLE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("John Doe"))
                .andExpect(jsonPath("$.vehicleType").value("CAR"))
                .andExpect(jsonPath("$.status").value("UNAVAILABLE"));

        verify(deliveryPersonService, times(1)).changeDeliveryPersonStatus(eq(1L), any());
    }

    @Test
    void testGetAllDeliveries() throws Exception {
        Page<DeliveryPersonResponseDto> page = new PageImpl<>(Arrays.asList(
                new DeliveryPersonResponseDto(1L, "John Doe", VehicleType.CAR, DeliveryPersonStatus.AVAILABLE),
                new DeliveryPersonResponseDto(2L, "Jane Doe", VehicleType.MOTORCYCLE, DeliveryPersonStatus.UNAVAILABLE)
        ));

        when(deliveryPersonService.findAllDeliveryPeople(any())).thenReturn(page);

        mockMvc.perform(get("/api/deliveryperson")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].nome").value("John Doe"))
                .andExpect(jsonPath("$.content[0].status").value("AVAILABLE"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].nome").value("Jane Doe"))
                .andExpect(jsonPath("$.content[1].status").value("UNAVAILABLE"));

        verify(deliveryPersonService, times(1)).findAllDeliveryPeople(any(Pageable.class));
    }

    @Test
    void testAssignAvailableDeliveryPeopleToPendingDeliveries() throws Exception {
        doNothing().when(deliveryPersonService).assignAvailableDeliveryPeopleToPendingDeliveries();

        mockMvc.perform(post("/api/deliveryperson/assign-available-delivery-people"))
                .andExpect(status().isOk());

        verify(deliveryPersonService, times(1)).assignAvailableDeliveryPeopleToPendingDeliveries();
    }

    @Test
    void testCalculateRoutes() throws Exception {
        DeliveryPersonResponseDto responseDto = new DeliveryPersonResponseDto(
                1L, "John Doe", VehicleType.CAR, DeliveryPersonStatus.UNAVAILABLE
        );
        when(deliveryPersonService.calculateRoutesByRegionResponse()).thenReturn((SortedMap<String, List<DeliveryResponseDto>>) responseDto);

        mockMvc.perform(get("/api/deliveryperson/calculate-routes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['Region A'][0].id").value(1L))
                .andExpect(jsonPath("$['Region A'][0].address").value("123 Street"))
                .andExpect(jsonPath("$['Region A'][0].status").value("Pending"));
        verify(deliveryPersonService, times(1)).calculateRoutesByRegionResponse();
    }
}
