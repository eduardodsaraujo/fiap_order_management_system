package br.com.fiap.customer_management.controller;


import br.com.fiap.customer_management.api.controller.CustomerController;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import br.com.fiap.customer_management.application.service.CustomerService;
import br.com.fiap.customer_management.application.dto.CustomerDTO;
import br.com.fiap.customer_management.application.dto.CustomerRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {


    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("John Doe");
        customerDTO.setEmail("john.doe@example.com");
        customerDTO.setPhone("123456789");
    }

    @Test
    void createCustomer_ShouldReturnCreatedCustomer() throws Exception {
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setName("John Doe");
        customerRequestDTO.setEmail("john.doe@example.com");
        customerRequestDTO.setPhone("123456789");

        when(customerService.saveCustomer(any(CustomerRequestDTO.class))).thenReturn(customerDTO);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"phone\":\"123456789\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phone").value("123456789"));

        verify(customerService, times(1)).saveCustomer(any(CustomerRequestDTO.class));
    }

    @Test
    void getAllCustomers_ShouldReturnListOfCustomers() throws Exception {
        when(customerService.findAll()).thenReturn(List.of(customerDTO));

        mockMvc.perform(get("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[0].phone").value("123456789"));

        verify(customerService, times(1)).findAll();
    }

    @Test
    void getCustomerById_ShouldReturnCustomer() throws Exception {
        when(customerService.findById(anyLong())).thenReturn(customerDTO);

        mockMvc.perform(get("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phone").value("123456789"));

        verify(customerService, times(1)).findById(1L);
    }

    @Test
    void updateCustomer_ShouldReturnUpdatedCustomer() throws Exception {
        // Cria o DTO de solicitação
        CustomerRequestDTO updatedCustomerRequestDTO = new CustomerRequestDTO();
        updatedCustomerRequestDTO.setName("Jane Doe");
        updatedCustomerRequestDTO.setEmail("jane.doe@example.com");
        updatedCustomerRequestDTO.setPhone("987654321");
        CustomerDTO updatedCustomerDTO = new CustomerDTO();
        updatedCustomerDTO.setId(1L);
        updatedCustomerDTO.setName("Jane Doe");
        updatedCustomerDTO.setEmail("jane.doe@example.com");
        updatedCustomerDTO.setPhone("987654321");

        when(customerService.updateCustomer(anyLong(), any(CustomerRequestDTO.class))).thenReturn(updatedCustomerDTO);
        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Jane Doe\",\"email\":\"jane.doe@example.com\",\"phone\":\"987654321\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane.doe@example.com"))
                .andExpect(jsonPath("$.phone").value("987654321"));
        verify(customerService, times(1)).updateCustomer(anyLong(), any(CustomerRequestDTO.class));
    }

    @Test
    void deleteCustomer_ShouldReturnNoContent() throws Exception {
        doNothing().when(customerService).deleteCustomer(anyLong());

        mockMvc.perform(delete("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).deleteCustomer(1L);
    }
}
