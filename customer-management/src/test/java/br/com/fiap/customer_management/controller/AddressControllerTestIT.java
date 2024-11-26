package br.com.fiap.customer_management.controller;



import br.com.fiap.customer_management.application.dto.AddressDTO;
import br.com.fiap.customer_management.application.dto.AddressRequestDTO;
import br.com.fiap.customer_management.api.controller.AddressController;
import br.com.fiap.customer_management.application.service.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
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


@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Test
    void addAddress_ShouldReturnSavedAddress() throws Exception {
        AddressDTO addressDTO = AddressDTO.builder()
                .id(1L)
                .street("Main Street")
                .number("123")
                .complement("Apt 101")
                .district("Downtown")
                .city("Metropolis")
                .state("NY")
                .postalCode("12345")
                .customerId(1L)
                .build();

        AddressRequestDTO addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setStreet("Main Street");
        addressRequestDTO.setNumber("123");
        addressRequestDTO.setComplement("Apt 101");
        addressRequestDTO.setDistrict("Downtown");
        addressRequestDTO.setCity("Metropolis");
        addressRequestDTO.setState("NY");
        addressRequestDTO.setPostalCode("12345");

        when(addressService.saveAddress(anyLong(), any(AddressRequestDTO.class))).thenReturn(addressDTO);

        mockMvc.perform(post("/api/customers/1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "street": "Main Street",
                                    "number": "123",
                                    "complement": "Apt 101",
                                    "district": "Downtown",
                                    "city": "Metropolis",
                                    "state": "NY",
                                    "postalCode": "12345"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.street", is("Main Street")))
                .andExpect(jsonPath("$.number", is("123")))
                .andExpect(jsonPath("$.complement", is("Apt 101")))
                .andExpect(jsonPath("$.district", is("Downtown")))
                .andExpect(jsonPath("$.city", is("Metropolis")))
                .andExpect(jsonPath("$.state", is("NY")))
                .andExpect(jsonPath("$.postalCode", is("12345")))
                .andExpect(jsonPath("$.customerId", is(1)));
    }

    @Test
    void getAllAddresses_ShouldReturnListOfAddresses() throws Exception {
        AddressDTO addressDTO = AddressDTO.builder()
                .id(1L)
                .street("Main Street")
                .number("123")
                .complement("Apt 101")
                .district("Downtown")
                .city("Metropolis")
                .state("NY")
                .postalCode("12345")
                .customerId(1L)
                .build();

        List<AddressDTO> addresses = Collections.singletonList(addressDTO);

        when(addressService.findAllAddresses(anyLong())).thenReturn(addresses);

        mockMvc.perform(get("/api/customers/1/addresses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].street", is("Main Street")))
                .andExpect(jsonPath("$[0].number", is("123")))
                .andExpect(jsonPath("$[0].complement", is("Apt 101")))
                .andExpect(jsonPath("$[0].district", is("Downtown")))
                .andExpect(jsonPath("$[0].city", is("Metropolis")))
                .andExpect(jsonPath("$[0].state", is("NY")))
                .andExpect(jsonPath("$[0].postalCode", is("12345")))
                .andExpect(jsonPath("$[0].customerId", is(1)));
    }

    @Test
    void getAddressById_ShouldReturnAddress() throws Exception {
        AddressDTO addressDTO = AddressDTO.builder()
                .id(1L)
                .street("Main Street")
                .number("123")
                .complement("Apt 101")
                .district("Downtown")
                .city("Metropolis")
                .state("NY")
                .postalCode("12345")
                .customerId(1L)
                .build();

        when(addressService.findAddressById(anyLong(), anyLong())).thenReturn(addressDTO);

        mockMvc.perform(get("/api/customers/1/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.street", is("Main Street")))
                .andExpect(jsonPath("$.number", is("123")))
                .andExpect(jsonPath("$.complement", is("Apt 101")))
                .andExpect(jsonPath("$.district", is("Downtown")))
                .andExpect(jsonPath("$.city", is("Metropolis")))
                .andExpect(jsonPath("$.state", is("NY")))
                .andExpect(jsonPath("$.postalCode", is("12345")))
                .andExpect(jsonPath("$.customerId", is(1)));
    }

    @Test
    void updateAddress_ShouldReturnUpdatedAddress() throws Exception {
        AddressDTO updatedAddress = AddressDTO.builder()
                .id(1L)
                .street("Updated Street")
                .number("456")
                .complement("Apt 202")
                .district("Uptown")
                .city("Gotham")
                .state("NY")
                .postalCode("67890")
                .customerId(1L)
                .build();

        AddressRequestDTO addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setStreet("Updated Street");
        addressRequestDTO.setNumber("456");
        addressRequestDTO.setComplement("Apt 202");
        addressRequestDTO.setDistrict("Uptown");
        addressRequestDTO.setCity("Gotham");
        addressRequestDTO.setState("NY");
        addressRequestDTO.setPostalCode("67890");

        when(addressService.updateAddress(anyLong(), anyLong(), any(AddressRequestDTO.class))).thenReturn(updatedAddress);

        mockMvc.perform(put("/api/customers/1/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "street": "Updated Street",
                                    "number": "456",
                                    "complement": "Apt 202",
                                    "district": "Uptown",
                                    "city": "Gotham",
                                    "state": "NY",
                                    "postalCode": "67890"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.street", is("Updated Street")))
                .andExpect(jsonPath("$.number", is("456")))
                .andExpect(jsonPath("$.complement", is("Apt 202")))
                .andExpect(jsonPath("$.district", is("Uptown")))
                .andExpect(jsonPath("$.city", is("Gotham")))
                .andExpect(jsonPath("$.state", is("NY")))
                .andExpect(jsonPath("$.postalCode", is("67890")))
                .andExpect(jsonPath("$.customerId", is(1)));
    }

    @Test
    void deleteAddress_ShouldReturnNoContent() throws Exception {
        doNothing().when(addressService).deleteAddress(anyLong(), anyLong());

        mockMvc.perform(delete("/api/customers/1/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }




}
