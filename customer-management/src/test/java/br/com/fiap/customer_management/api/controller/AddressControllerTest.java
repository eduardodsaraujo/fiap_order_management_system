package br.com.fiap.customer_management.api.controller;

import br.com.fiap.customer_management.application.AddressDTO;
import br.com.fiap.customer_management.application.AddressRequestDTO;
import br.com.fiap.customer_management.application.service.AddressService;
import br.com.fiap.customer_management.infrastructure.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static br.com.fiap.customer_management.utils.AddressHelper.createAddressDTO;
import static br.com.fiap.customer_management.utils.AddressHelper.createAddressRequestDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AddressControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AddressService addressService;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        AddressController addressController = new AddressController(addressService);
        mockMvc = MockMvcBuilders.standaloneSetup(addressController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void shouldAddAddress() throws Exception {
        var customerId = 1L;
        var addressRequestDTO = createAddressRequestDTO();
        var addressDTO = createAddressDTO(customerId);

        when(addressService.saveAddress(eq(customerId), any(AddressRequestDTO.class)))
                .thenReturn(addressDTO);

        mockMvc.perform(post("/api/customers/{customerId}/addresses", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addressRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(addressDTO.getId()))
                .andExpect(jsonPath("$.street").value(addressDTO.getStreet()));

        verify(addressService, times(1)).saveAddress(eq(customerId), any(AddressRequestDTO.class));
    }

    @Test
    void shouldGetAllAddresses() throws Exception {
        var customerId = 1L;
        var addressDTO = createAddressDTO(1L);
        List<AddressDTO> addresses = Arrays.asList(addressDTO);

        when(addressService.findAllAddresses(customerId)).thenReturn(addresses);

        mockMvc.perform(get("/api/customers/{customerId}/addresses", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(addressDTO.getId()))
                .andExpect(jsonPath("$[0].street").value(addressDTO.getStreet()));

        verify(addressService, times(1)).findAllAddresses(customerId);
    }

    @Test
    void shouldGetAddressById() throws Exception {
        var customerId = 1L;
        var addressId = 1L;
        var addressDTO = createAddressDTO(addressId);

        when(addressService.findAddressById(customerId, addressId)).thenReturn(addressDTO);

        mockMvc.perform(get("/api/customers/{customerId}/addresses/{addressId}", customerId, addressId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(addressDTO.getId()))
                .andExpect(jsonPath("$.street").value(addressDTO.getStreet()));

        verify(addressService, times(1)).findAddressById(customerId, addressId);
    }

    @Test
    void shouldUpdateAddress() throws Exception {
        var customerId = 1L;
        var addressId = 1L;
        var addressRequestDTO = createAddressRequestDTO();
        var updatedAddressDTO = createAddressDTO(addressId);

        when(addressService.updateAddress(eq(customerId), eq(addressId), any(AddressRequestDTO.class)))
                .thenReturn(updatedAddressDTO);

        mockMvc.perform(put("/api/customers/{customerId}/addresses/{addressId}", customerId, addressId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addressRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedAddressDTO.getId()))
                .andExpect(jsonPath("$.street").value(updatedAddressDTO.getStreet()));

        verify(addressService, times(1)).updateAddress(eq(customerId), eq(addressId), any(AddressRequestDTO.class));
    }

    @Test
    void shouldDeleteAddress() throws Exception {
        var customerId = 1L;
        var addressId = 1L;

        doNothing().when(addressService).deleteAddress(customerId, addressId);

        mockMvc.perform(delete("/api/customers/{customerId}/addresses/{addressId}", customerId, addressId))
                .andExpect(status().isNoContent());

        verify(addressService, times(1)).deleteAddress(customerId, addressId);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
