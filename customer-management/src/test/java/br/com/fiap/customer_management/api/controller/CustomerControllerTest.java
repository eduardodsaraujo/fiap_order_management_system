package br.com.fiap.customer_management.api.controller;

import br.com.fiap.customer_management.api.controller.CustomerController;
import br.com.fiap.customer_management.application.CustomerDTO;
import br.com.fiap.customer_management.application.CustomerRequestDTO;
import br.com.fiap.customer_management.application.service.CustomerService;
import br.com.fiap.customer_management.infrastructure.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static br.com.fiap.customer_management.utils.CustomerHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        CustomerController customerController = new CustomerController(customerService);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @Test
    void shouldGetAllCustomers() throws Exception {
        // Arrange
        CustomerDTO customer1 = createCustomerDTO(1L);
        CustomerDTO customer2 = createCustomerDTO(2L);
        List<CustomerDTO> customers = Arrays.asList(customer1, customer2);

        when(customerService.findAll()).thenReturn(customers);

        // Act & Assert
        mockMvc.perform(get("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(customer1.getId()))
                .andExpect(jsonPath("$[0].name").value(customer1.getName()))
                .andExpect(jsonPath("$[0].email").value(customer1.getEmail()))
                .andExpect(jsonPath("$[0].phone").value(customer1.getPhone()))
                .andExpect(jsonPath("$[1].id").value(customer2.getId()))
                .andExpect(jsonPath("$[1].name").value(customer2.getName()))
                .andExpect(jsonPath("$[1].email").value(customer2.getEmail()))
                .andExpect(jsonPath("$[1].phone").value(customer2.getPhone()));


        verify(customerService, times(1)).findAll();
    }

    @Test
    void shouldCreateCustomer() throws Exception {
        // Arrange
        CustomerRequestDTO customerRequest = createCustomerRequestDTO();
        CustomerDTO savedCustomer = createCustomerDTO(1L);

        when(customerService.saveCustomer(any(CustomerRequestDTO.class))).thenReturn(savedCustomer);

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedCustomer.getId()))
                .andExpect(jsonPath("$.name").value(savedCustomer.getName()))
                .andExpect(jsonPath("$.email").value(savedCustomer.getEmail()))
                .andExpect(jsonPath("$.phone").value(savedCustomer.getPhone()));

        verify(customerService, times(1)).saveCustomer(any(CustomerRequestDTO.class));
    }

    @Test
    void shouldGetCustomerById() throws Exception {
        // Arrange
        Long id = 1L;
        CustomerDTO customer = createCustomerDTO(id);

        when(customerService.findById(id)).thenReturn(customer);

        // Act & Assert
        mockMvc.perform(get("/api/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customer.getId()))
                .andExpect(jsonPath("$.name").value(customer.getName()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()))
                .andExpect(jsonPath("$.phone").value(customer.getPhone()));

        verify(customerService, times(1)).findById(id);
    }

    @Test
    void shouldUpdateCustomer() throws Exception {
        // Arrange
        Long id = 1L;
        CustomerRequestDTO customerRequest = createCustomerRequestDTO();
        CustomerDTO updatedCustomer = createCustomerDTO(1L);

        when(customerService.updateCustomer(eq(id), any(CustomerRequestDTO.class))).thenReturn(updatedCustomer);

        // Act & Assert
        mockMvc.perform(put("/api/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerRequest)))
                //.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedCustomer.getId()))
                .andExpect(jsonPath("$.name").value(updatedCustomer.getName()))
                .andExpect(jsonPath("$.email").value(updatedCustomer.getEmail()))
                .andExpect(jsonPath("$.phone").value(updatedCustomer.getPhone()));

        verify(customerService, times(1)).updateCustomer(eq(id), any(CustomerRequestDTO.class));
    }

    @Test
    void shouldDeleteCustomer() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(customerService).deleteCustomer(id);

        // Act & Assert
        mockMvc.perform(delete("/api/customers/{id}", id))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).deleteCustomer(id);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
