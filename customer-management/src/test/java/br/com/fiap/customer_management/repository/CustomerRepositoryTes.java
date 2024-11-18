package br.com.fiap.customer_management.repository;

import br.com.fiap.customer_management.domain.model.Customer;
import br.com.fiap.customer_management.domain.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CustomerRepositoryTes {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void shouldSaveCustomer() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhone("123456789");

        Customer savedCustomer = customerRepository.save(customer);

        assertNotNull(savedCustomer.getId());
        assertEquals("John Doe", savedCustomer.getName());
        assertEquals("john.doe@example.com", savedCustomer.getEmail());
    }

    @Test
    void shouldFindCustomerById() {
        Customer customer = new Customer();
        customer.setName("Jane Doe");
        customer.setEmail("jane.doe@example.com");
        customer.setPhone("987654321");

        Customer savedCustomer = customerRepository.save(customer);
        Optional<Customer> foundCustomer = customerRepository.findById(savedCustomer.getId());

        assertTrue(foundCustomer.isPresent());
        assertEquals("Jane Doe", foundCustomer.get().getName());
    }

    @Test
    void shouldFindAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setName("Customer One");
        customer1.setEmail("one@example.com");
        customer1.setPhone("111111111");

        Customer customer2 = new Customer();
        customer2.setName("Customer Two");
        customer2.setEmail("two@example.com");
        customer2.setPhone("222222222");

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        List<Customer> customers = customerRepository.findAll();

        assertEquals(2, customers.size());
    }

    @Test
    void shouldUpdateCustomer() {
        Customer customer = new Customer();
        customer.setName("Old Name");
        customer.setEmail("old.email@example.com");
        customer.setPhone("000000000");

        Customer savedCustomer = customerRepository.save(customer);
        savedCustomer.setName("New Name");
        savedCustomer.setEmail("new.email@example.com");

        Customer updatedCustomer = customerRepository.save(savedCustomer);

        assertEquals("New Name", updatedCustomer.getName());
        assertEquals("new.email@example.com", updatedCustomer.getEmail());
    }

    @Test
    void shouldDeleteCustomer() {
        Customer customer = new Customer();
        customer.setName("Delete Me");
        customer.setEmail("delete.me@example.com");
        customer.setPhone("333333333");

        Customer savedCustomer = customerRepository.save(customer);
        Long customerId = savedCustomer.getId();

        customerRepository.deleteById(customerId);

        Optional<Customer> deletedCustomer = customerRepository.findById(customerId);
        assertFalse(deletedCustomer.isPresent());
    }
}
