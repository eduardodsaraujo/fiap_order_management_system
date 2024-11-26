package br.com.fiap.customer_management.repository;

import br.com.fiap.customer_management.domain.model.Address;
import br.com.fiap.customer_management.domain.model.Customer;
import br.com.fiap.customer_management.domain.repository.AddressRepository;
import br.com.fiap.customer_management.domain.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTestIT {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Customer customer1;
    private Address address1;
    private Address address2;

    @BeforeEach
    void setUp() {
        customer1 = new Customer();
        customer1.setName("Customer 1");
        customer1.setEmail("customer1@example.com");
        customer1.setPhone("123456789");

        customer1 = customerRepository.save(customer1);
        address1 = new Address();
        address1.setStreet("Street 1");
        address1.setNumber("123");
        address1.setCity("City 1");
        address1.setState("State 1");
        address1.setPostalCode("12345");
        address1.setCustomer(customer1);

        address2 = new Address();
        address2.setStreet("Street 2");
        address2.setNumber("456");
        address2.setCity("City 2");
        address2.setState("State 2");
        address2.setPostalCode("67890");
        address2.setCustomer(customer1);

        addressRepository.save(address1);
        addressRepository.save(address2);
    }

    @Test
    void testSaveCustomerWithAddresses() {
        Optional<Customer> savedCustomer = customerRepository.findById(customer1.getId());
        assertThat(savedCustomer).isPresent();
        assertThat(savedCustomer.get().getName()).isEqualTo("Customer 1");
        List<Address> addresses = addressRepository.findByCustomer_Id(customer1.getId());
        assertThat(addresses).hasSize(2);
        assertThat(addresses.get(0).getStreet()).isEqualTo("Street 1");
        assertThat(addresses.get(1).getStreet()).isEqualTo("Street 2");
    }

    @Test
    void testUpdateCustomerWithAddress() {
        customer1.setPhone("987654321");
        customerRepository.save(customer1);
        Optional<Customer> updatedCustomer = customerRepository.findById(customer1.getId());
        assertThat(updatedCustomer).isPresent();
        assertThat(updatedCustomer.get().getPhone()).isEqualTo("987654321");
    }

    @Test
    void testDeleteCustomerWithAddresses() {
        customerRepository.delete(customer1);

        Optional<Customer> deletedCustomer = customerRepository.findById(customer1.getId());
        assertThat(deletedCustomer).isNotPresent();
        List<Address> deletedAddresses = addressRepository.findByCustomer_Id(customer1.getId());
        assertThat(deletedAddresses).isEmpty();
    }

    @Test
    void testFindAddressByCustomerId() {
        List<Address> addresses = addressRepository.findByCustomer_Id(customer1.getId());
        assertThat(addresses).hasSize(2);
        assertThat(addresses.get(0).getStreet()).isEqualTo("Street 1");
    }

}
