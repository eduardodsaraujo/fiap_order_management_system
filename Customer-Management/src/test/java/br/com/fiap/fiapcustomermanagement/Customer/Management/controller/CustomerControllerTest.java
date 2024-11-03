package br.com.fiap.fiapcustomermanagement.Customer.Management.controller;


import br.com.fiap.fiapcustomermanagement.Customer.Management.api.controller.CustomerController;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import br.com.fiap.fiapcustomermanagement.Customer.Management.application.service.CustomerService;
import br.com.fiap.fiapcustomermanagement.Customer.Management.application.service.dto.CustomerDTO;
import br.com.fiap.fiapcustomermanagement.Customer.Management.application.service.dto.CustomerRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    void getAllCustomers_ShouldReturnListOfCustomers() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("John Doe");

        List<CustomerDTO> customers = Collections.singletonList(customerDTO);

        when(customerService.findAll()).thenReturn(customers);

        mockMvc.perform(get("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")));
    }

    @Test
    void createCustomer_ShouldReturnSavedCustomer() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("John Doe");

        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setName("John Doe");

        when(customerService.saveCustomer(any(CustomerRequestDTO.class))).thenReturn(customerDTO);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    void getCustomerById_ShouldReturnCustomer() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("John Doe");

        when(customerService.findById(anyLong())).thenReturn(customerDTO);

        mockMvc.perform(get("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    void updateCustomer_ShouldReturnUpdatedCustomer() throws Exception {
        CustomerDTO updatedCustomer = new CustomerDTO();
        updatedCustomer.setId(1L);
        updatedCustomer.setName("John Doe Updated");

        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setName("John Doe Updated");

        when(customerService.updateCustomer(anyLong(), any(CustomerRequestDTO.class))).thenReturn(updatedCustomer);

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe Updated\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe Updated")));
    }

    @Test
    void deleteCustomer_ShouldReturnNoContent() throws Exception {
        doNothing().when(customerService).deleteCustomer(anyLong());

        mockMvc.perform(delete("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
