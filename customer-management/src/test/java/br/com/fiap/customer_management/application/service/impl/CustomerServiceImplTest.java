package br.com.fiap.customer_management.application.service.impl;

import br.com.fiap.customer_management.application.CustomerDTO;
import br.com.fiap.customer_management.domain.model.Customer;
import br.com.fiap.customer_management.domain.repository.CustomerRepository;
import br.com.fiap.customer_management.infrastructure.exception.CustomerException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static br.com.fiap.customer_management.utils.CustomerHelper.createCustomer;
import static br.com.fiap.customer_management.utils.CustomerHelper.createCustomerRequestDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        customerService = new CustomerServiceImpl(customerRepository);
    }

    @Test
    void shouldFindAllCustomers() {
        // Arrange
        var customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("John Doe");
        var customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Jane Smith");

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        // Act
        var customers = customerService.findAll();

        // Assert
        Assertions.assertThat(customers)
                .hasSize(2)
                .extracting(CustomerDTO::getName)
                .containsExactlyInAnyOrder("John Doe", "Jane Smith");
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void shouldSaveCustomer() {
        // Arrange
        var customerRequest = createCustomerRequestDTO();

        var savedCustomer = createCustomer();

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        // Act
        var customerDTO = customerService.saveCustomer(customerRequest);

        // Assert
        Assertions.assertThat(customerDTO)
                .isNotNull()
                .extracting(CustomerDTO::getId, CustomerDTO::getName, CustomerDTO::getEmail,CustomerDTO::getPhone)
                .containsExactly(1L, customerRequest.getName(), customerRequest.getEmail(), customerRequest.getPhone());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void shouldFindCustomerById() {
        // Arrange
        var customer = createCustomer();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // Act
        var customerDTO = customerService.findById(1L);

        // Assert
        Assertions.assertThat(customerDTO)
                .isNotNull()
                .extracting(CustomerDTO::getId, CustomerDTO::getName)
                .containsExactly(1L, "Eduardo");
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> customerService.findById(1L))
                .isInstanceOf(CustomerException.class)
                .hasMessage("Customer not found");
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void shouldDeleteCustomer() {
        // Arrange
        var customer = new Customer();
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).deleteById(1L);

        // Act
        customerService.deleteCustomer(1L);

        // Assert
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).deleteById(1L);
    }
}
