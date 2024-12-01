package br.com.fiap.delivery_logistics.controller;
import java.util.TreeMap;
import br.com.fiap.delivery_logistics.api.controller.DeliveryPersonController;
import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryResponseDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.ChangeDeliveryPersonStatusRequestDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonRequestDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonResponseDto;
import br.com.fiap.delivery_logistics.application.service.DeliveryPersonService;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DeliveryPersonControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DeliveryPersonService deliveryPersonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        DeliveryPersonController deliveryPersonController = new DeliveryPersonController(deliveryPersonService);
        mockMvc = MockMvcBuilders.standaloneSetup(deliveryPersonController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())

                .build();
    }

    @Test
    void shouldCreateDeliveryPerson() throws Exception {
        // Arrange
        DeliveryPersonRequestDto deliveryPersonRequestDto = new DeliveryPersonRequestDto();
        DeliveryPersonResponseDto savedDeliveryPerson = new DeliveryPersonResponseDto(1L, "John Doe", null, null);

        when(deliveryPersonService.createDeliveryPerson(any(DeliveryPersonRequestDto.class)))
                .thenReturn(savedDeliveryPerson);

        // Act & Assert
        mockMvc.perform(post("/api/deliveryperson")
                        .contentType("application/json")
                        .content(asJsonString(deliveryPersonRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(savedDeliveryPerson.getId()))
                .andExpect(jsonPath("$.name").value(savedDeliveryPerson.getName()));

        verify(deliveryPersonService, times(1)).createDeliveryPerson(any(DeliveryPersonRequestDto.class));
    }

    @Test
    void shouldUpdateDeliveryPersonStatus() throws Exception {
        // Arrange
        Long id = 1L;
        ChangeDeliveryPersonStatusRequestDto statusRequest = new ChangeDeliveryPersonStatusRequestDto();
        DeliveryPersonResponseDto updatedDeliveryPerson = new DeliveryPersonResponseDto(id, "John Doe", null, null);

        when(deliveryPersonService.changeDeliveryPersonStatus(eq(id), any(ChangeDeliveryPersonStatusRequestDto.class)))
                .thenReturn(updatedDeliveryPerson);

        // Act & Assert
        mockMvc.perform(put("/api/deliveryperson/{id}", id)
                        .contentType("application/json")
                        .content(asJsonString(statusRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedDeliveryPerson.getId()))
                .andExpect(jsonPath("$.name").value(updatedDeliveryPerson.getName()));

        verify(deliveryPersonService, times(1)).changeDeliveryPersonStatus(eq(id), any(ChangeDeliveryPersonStatusRequestDto.class));
    }

    @Test
    void shouldGetAllDeliveryPersons() throws Exception {
        // Arrange
        DeliveryPersonResponseDto deliveryPerson1 = new DeliveryPersonResponseDto(1L, "John Doe", null, null);
        DeliveryPersonResponseDto deliveryPerson2 = new DeliveryPersonResponseDto(2L, "Jane Doe", null, null);
        Pageable pageable = PageRequest.of(0, 10); // Page 0 with 10 elements per page
        List<DeliveryPersonResponseDto> deliveryPeopleList = Arrays.asList(deliveryPerson1, deliveryPerson2);

        Page<DeliveryPersonResponseDto> deliveryPeople =
                new PageImpl<>(deliveryPeopleList, pageable, deliveryPeopleList.size());


        when(deliveryPersonService.findAllDeliveryPeople(any(PageRequest.class)))
                .thenReturn(deliveryPeople);

        // Act & Assert
        mockMvc.perform(get("/api/deliveryperson",pageable)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(deliveryPersonService, times(1)).findAllDeliveryPeople(any(PageRequest.class));
    }

    @Test
    void shouldAssignAvailableDeliveryPeopleToPendingDeliveries() throws Exception {
        // Arrange
        doNothing().when(deliveryPersonService).assignAvailableDeliveryPeopleToPendingDeliveries();

        // Act & Assert
        mockMvc.perform(post("/api/deliveryperson/assign-available-delivery-people"))
                .andExpect(status().isOk());

        verify(deliveryPersonService, times(1)).assignAvailableDeliveryPeopleToPendingDeliveries();
    }

    @Test
    void shouldCalculateRoutes() throws Exception {
        // Arrange
        DeliveryResponseDto deliveryResponseDto = new DeliveryResponseDto();
        Map<String, List<DeliveryResponseDto>> routes = new TreeMap<>();
        routes.put("Region1", List.of(deliveryResponseDto));

        when(deliveryPersonService.calculateRoutesByRegionResponse()).thenReturn((SortedMap<String, List<DeliveryResponseDto>>) routes);

        // Act & Assert
        mockMvc.perform(get("/api/deliveryperson/calculate-routes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Region1").isArray());

        verify(deliveryPersonService, times(1)).calculateRoutesByRegionResponse();
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
