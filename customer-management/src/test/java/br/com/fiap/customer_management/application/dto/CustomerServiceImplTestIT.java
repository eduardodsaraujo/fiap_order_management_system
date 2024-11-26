package br.com.fiap.customer_management.application.dto;


import br.com.fiap.customer_management.application.dto.CustomerDTO;
import br.com.fiap.customer_management.application.dto.CustomerRequestDTO;
import br.com.fiap.customer_management.application.service.impl.CustomerServiceImpl;
import br.com.fiap.customer_management.domain.model.Customer;
import br.com.fiap.customer_management.domain.repository.CustomerRepository;
import br.com.fiap.customer_management.infrastructure.exception.CustomerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
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
    void findAll_ShouldReturnListOfCustomerDTOs() {
        List<CustomerDTO> customers = customerService.findAll();

        assertNotNull(customers);
        assertEquals(1, customers.size());
        assertEquals("John Doe", customers.get(0).getName());
    }

    @Test
    void saveCustomer_ShouldReturnSavedCustomerDTO() {
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
    void findById_ShouldReturnCustomerDTO_WhenCustomerExists() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO result = customerService.findById(customer.getId());

        assertNotNull(result);
        assertEquals(customer.getName(), result.getName());
        assertEquals(customer.getEmail(), result.getEmail());
    }

    @Test
    void findById_ShouldThrowCustomerException_WhenCustomerNotFound() {
        Exception exception = assertThrows(CustomerException.class, () -> customerService.findById(999L));
        assertEquals("Customer not found", exception.getMessage());
    }

    @Test
    void updateCustomer_ShouldReturnUpdatedCustomerDTO() {
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
    void deleteCustomer_ShouldRemoveCustomer_WhenCustomerExists() {
        Customer customer = customerRepository.findAll().get(0);

        customerService.deleteCustomer(customer.getId());

        Optional<Customer> deletedCustomer = customerRepository.findById(customer.getId());
        assertFalse(deletedCustomer.isPresent());
    }


}
