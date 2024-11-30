package br.com.fiap.customer_management.domain.repository;

import br.com.fiap.customer_management.domain.model.Customer;
import br.com.fiap.customer_management.domain.repository.CustomerRepository;
import br.com.fiap.customer_management.utils.CustomerHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static br.com.fiap.customer_management.utils.CustomerHelper.createCustomer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerRepositoryTest {

    @Mock
    private CustomerRepository customerRepository;

    private AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void shouldSaveCustomer() {
        // Arrange
        var customer = createCustomer();

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Act
        var savedCustomer = customerRepository.save(customer);

        // Assert
        assertThat(savedCustomer)
                .isNotNull()
                .isEqualTo(customer);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void shouldFindCustomerById() {
        // Arrange
        Long id = 1L;
        var customer = createCustomer();

        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        // Act
        var foundCustomerOptional = customerRepository.findById(id);

        // Assert
        assertThat(foundCustomerOptional).isPresent().containsSame(customer);
        foundCustomerOptional.ifPresent(foundCustomer -> {
            assertThat(foundCustomer.getId()).isEqualTo(customer.getId());
            assertThat(foundCustomer.getName()).isEqualTo(customer.getName());
            assertThat(foundCustomer.getEmail()).isEqualTo(customer.getEmail());
            assertThat(foundCustomer.getPhone()).isEqualTo(customer.getPhone());
        });
        verify(customerRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void shouldDeleteCustomerById() {
        // Arrange
        Long id = 1L;
        doNothing().when(customerRepository).deleteById(any(Long.class));

        // Act
        customerRepository.deleteById(id);

        // Assert
        verify(customerRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldFindAllCustomers() {
        // Arrange
        var customer1 = createCustomer();
        customer1.setId(1L);

        var customer2 = createCustomer();
        customer2.setId(2L);

        var listCustomers = Arrays.asList(customer1, customer2);
        when(customerRepository.findAll()).thenReturn(listCustomers);

        // Act
        var foundCustomers = customerRepository.findAll();

        // Assert
        assertThat(foundCustomers)
                .hasSize(2)
                .containsExactlyInAnyOrder(customer1, customer2);
        verify(customerRepository, times(1)).findAll();
    }
}
