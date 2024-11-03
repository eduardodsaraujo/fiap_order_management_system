package br.com.fiap.fiapcustomermanagement.Customer.Management.controller;

import br.com.fiap.fiapcustomermanagement.Customer.Management.api.controller.AddressController;
import br.com.fiap.fiapcustomermanagement.Customer.Management.application.service.AddressService;
import br.com.fiap.fiapcustomermanagement.Customer.Management.application.service.dto.AddressDTO;
import br.com.fiap.fiapcustomermanagement.Customer.Management.application.service.dto.AddressRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressController.class)
public class AddressControllerIntegrationTes {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Test
    void addAddress_ShouldReturnSavedAddress() throws Exception {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(1L);

        AddressRequestDTO addressRequestDTO = new AddressRequestDTO();

        when(addressService.saveAddress(anyLong(), any(AddressRequestDTO.class))).thenReturn(addressDTO);

        mockMvc.perform(post("/api/customers/1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getAllAddresses_ShouldReturnListOfAddresses() throws Exception {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(1L);
        List<AddressDTO> addresses = Collections.singletonList(addressDTO);

        when(addressService.findAllAddresses(anyLong())).thenReturn(addresses);

        mockMvc.perform(get("/api/customers/1/addresses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getAddressById_ShouldReturnAddress() throws Exception {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(1L);

        when(addressService.findAddressById(anyLong(), anyLong())).thenReturn(addressDTO);

        mockMvc.perform(get("/api/customers/1/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void updateAddress_ShouldReturnUpdatedAddress() throws Exception {
        AddressDTO updatedAddress = new AddressDTO();
        updatedAddress.setId(1L);

        AddressRequestDTO addressRequestDTO = new AddressRequestDTO();

        when(addressService.updateAddress(anyLong(), anyLong(), any(AddressRequestDTO.class))).thenReturn(updatedAddress);

        mockMvc.perform(put("/api/customers/1/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void deleteAddress_ShouldReturnNoContent() throws Exception {
        doNothing().when(addressService).deleteAddress(anyLong(), anyLong());

        mockMvc.perform(delete("/api/customers/1/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
