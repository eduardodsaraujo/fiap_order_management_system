package br.com.fiap.customer_management.controller;

import br.com.fiap.customer_management.api.controller.CustomerController;
import br.com.fiap.customer_management.application.dto.AddressDTO;
import br.com.fiap.customer_management.application.dto.CustomerDTO;
import br.com.fiap.customer_management.application.dto.CustomerRequestDTO;
import br.com.fiap.customer_management.application.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class CustomerControllerTestIT {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private CustomerDTO customerDTO;
    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        addressDTO = new AddressDTO();
        addressDTO.setId(1L);
        addressDTO.setStreet("Main St");
        addressDTO.setNumber("123");
        addressDTO.setComplement("Apt 4");
        addressDTO.setDistrict("Central");
        addressDTO.setCity("Metropolis");
        addressDTO.setState("StateX");
        addressDTO.setPostalCode("12345");
        addressDTO.setCustomerId(1L);
        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("John Doe");
        customerDTO.setEmail("john.doe@example.com");
        customerDTO.setPhone("123456789");
        customerDTO.setAddresses(List.of(addressDTO));
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
                .andExpect(jsonPath("$.phone").value("123456789"))
                .andExpect(jsonPath("$.addresses[0].id").value(1L)) // Verificando se o endereço foi incluído
                .andExpect(jsonPath("$.addresses[0].street").value("Main St"));

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
                .andExpect(jsonPath("$[0].phone").value("123456789"))
                .andExpect(jsonPath("$[0].addresses[0].id").value(1L));

        verify(customerService, times(1)).findAll();
    }

    @Test
    void getCustomerById_ShouldReturnCustomerWithAddresses() throws Exception {
        when(customerService.findById(anyLong())).thenReturn(customerDTO);

        mockMvc.perform(get("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phone").value("123456789"))
                .andExpect(jsonPath("$.addresses[0].id").value(1L))
                .andExpect(jsonPath("$.addresses[0].street").value("Main St"));

        verify(customerService, times(1)).findById(1L);
    }

    @Test
    void updateCustomer_ShouldReturnUpdatedCustomer() throws Exception {
        CustomerRequestDTO updatedCustomerRequestDTO = new CustomerRequestDTO();
        updatedCustomerRequestDTO.setName("Jane Doe");
        updatedCustomerRequestDTO.setEmail("jane.doe@example.com");
        updatedCustomerRequestDTO.setPhone("987654321");

        CustomerDTO updatedCustomerDTO = new CustomerDTO();
        updatedCustomerDTO.setId(1L);
        updatedCustomerDTO.setName("Jane Doe");
        updatedCustomerDTO.setEmail("jane.doe@example.com");
        updatedCustomerDTO.setPhone("987654321");
        updatedCustomerDTO.setAddresses(List.of(addressDTO));

        when(customerService.updateCustomer(anyLong(), any(CustomerRequestDTO.class))).thenReturn(updatedCustomerDTO);

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Jane Doe\",\"email\":\"jane.doe@example.com\",\"phone\":\"987654321\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane.doe@example.com"))
                .andExpect(jsonPath("$.phone").value("987654321"))
                .andExpect(jsonPath("$.addresses[0].id").value(1L));

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
