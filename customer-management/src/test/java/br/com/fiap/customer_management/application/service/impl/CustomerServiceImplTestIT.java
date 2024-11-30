package br.com.fiap.customer_management.application.service.impl;


import br.com.fiap.customer_management.application.CustomerDTO;
import br.com.fiap.customer_management.application.CustomerRequestDTO;
import br.com.fiap.customer_management.domain.model.Customer;
import br.com.fiap.customer_management.domain.repository.CustomerRepository;
import br.com.fiap.customer_management.infrastructure.exception.CustomerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class CustomerServiceImplTestIT {

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhone("123456789");
        customerRepository.save(customer);
    }

    @Test
    void shouldReturnListOfCustomerDTOs() {
        List<CustomerDTO> customers = customerService.findAll();

        assertNotNull(customers);
        assertEquals(2, customers.size());
    }

    @Test
    void shouldReturnSavedCustomerDTO() {
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setName("Jane Doe");
        customerRequestDTO.setEmail("jane.doe@example.com");
        customerRequestDTO.setPhone("987654321");

        CustomerDTO result = customerService.saveCustomer(customerRequestDTO);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("jane.doe@example.com", result.getEmail());
        assertEquals("987654321", result.getPhone());
    }

    @Test
    void shouldReturnCustomerDTO_WhenCustomerExists() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO result = customerService.findById(customer.getId());

        assertNotNull(result);
        assertEquals(customer.getName(), result.getName());
        assertEquals(customer.getEmail(), result.getEmail());
    }

    @Test
    void shouldThrowCustomerException_WhenCustomerNotFound() {
        Exception exception = assertThrows(CustomerException.class, () -> customerService.findById(999L));
        assertEquals("Customer not found", exception.getMessage());
    }

    @Test
    void shouldReturnUpdatedCustomerDTO() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setName("Updated Name");
        customerRequestDTO.setEmail("updated.email@example.com");
        customerRequestDTO.setPhone("987654321");

        CustomerDTO result = customerService.updateCustomer(customer.getId(), customerRequestDTO);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("updated.email@example.com", result.getEmail());
    }

    @Test
    void shouldRemoveCustomer_WhenCustomerExists() {
        Customer customer = customerRepository.findAll().get(0);

        customerService.deleteCustomer(customer.getId());

        Optional<Customer> deletedCustomer = customerRepository.findById(customer.getId());
        assertFalse(deletedCustomer.isPresent());
    }


}
