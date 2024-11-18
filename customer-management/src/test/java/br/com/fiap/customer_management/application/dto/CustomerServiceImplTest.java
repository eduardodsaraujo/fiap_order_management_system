package br.com.fiap.customer_management.application.dto;

import br.com.fiap.customer_management.application.service.impl.CustomerServiceImpl;
import br.com.fiap.customer_management.domain.model.Customer;
import br.com.fiap.customer_management.domain.repository.CustomerRepository;
import br.com.fiap.customer_management.infrastructure.exception.CustomerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ShouldReturnListOfCustomerDTOs() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));

        List<CustomerDTO> customers = customerService.findAll();

        assertEquals(1, customers.size());
        assertEquals("John Doe", customers.get(0).getName());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void saveCustomer_ShouldReturnSavedCustomerDTO() {
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setName("John Doe");
        customerRequestDTO.setEmail("john.doe@example.com");
        customerRequestDTO.setPhone("123456789");

        Customer savedCustomer = new Customer();
        savedCustomer.setId(1L);
        savedCustomer.setName("John Doe");
        savedCustomer.setEmail("john.doe@example.com");
        savedCustomer.setPhone("123456789");

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerDTO result = customerService.saveCustomer(customerRequestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void findById_ShouldReturnCustomerDTO_WhenCustomerExists() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        CustomerDTO result = customerService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        verify(customerRepository, times(1)).findById(anyLong());
    }

    @Test
    void findById_ShouldThrowCustomerException_WhenCustomerNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerException.class, () -> customerService.findById(1L));

        assertEquals("Customer not found", exception.getMessage());
        verify(customerRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateCustomer_ShouldReturnUpdatedCustomerDTO() {
        Customer existingCustomer = new Customer();
        existingCustomer.setId(1L);
        existingCustomer.setName("John Doe");

        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setName("Jane Doe");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(1L);
        updatedCustomer.setName("Jane Doe");

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        CustomerDTO result = customerService.updateCustomer(1L, customerRequestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Jane Doe", result.getName());
        verify(customerRepository, times(1)).findById(anyLong());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void deleteCustomer_ShouldCallDeleteById_WhenCustomerExists() {
        Customer customer = new Customer();
        customer.setId(1L);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1L);

        verify(customerRepository, times(1)).findById(anyLong());
        verify(customerRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteCustomer_ShouldThrowCustomerException_WhenCustomerNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerException.class, () -> customerService.deleteCustomer(1L));

        assertEquals("Customer not found", exception.getMessage());
        verify(customerRepository, times(1)).findById(anyLong());
        verify(customerRepository, never()).deleteById(anyLong());
    }
}
