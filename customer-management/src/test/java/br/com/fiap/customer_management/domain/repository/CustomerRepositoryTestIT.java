package br.com.fiap.customer_management.domain.repository;

import br.com.fiap.customer_management.domain.model.Address;
import br.com.fiap.customer_management.domain.model.Customer;
import br.com.fiap.customer_management.domain.repository.AddressRepository;
import br.com.fiap.customer_management.domain.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static br.com.fiap.customer_management.utils.AddressHelper.createAddress;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class CustomerRepositoryTestIT {

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
        customer1.setPhone("21123456789");

        customer1 = customerRepository.save(customer1);
        address1 = createAddress();
        address1.setCustomer(customer1);

        address2 = createAddress();
        address2.setStreet("Rua das Flores 2");
        address2.setCustomer(customer1);

        addressRepository.save(address1);
        addressRepository.save(address2);

        customer1 = customerRepository.findById(customer1.getId()).get();
    }

    @Test
    void shouldSaveCustomerWithAddresses() {
        Optional<Customer> savedCustomer = customerRepository.findById(customer1.getId());
        assertThat(savedCustomer).isPresent();
        assertThat(savedCustomer.get().getName()).isEqualTo("Customer 1");
        List<Address> addresses = addressRepository.findByCustomer_Id(customer1.getId());
        assertThat(addresses).hasSize(2);
        assertThat(addresses.get(0).getStreet()).isEqualTo("Rua das Flores");
        assertThat(addresses.get(1).getStreet()).isEqualTo("Rua das Flores 2");
    }

    @Test
    void shouldUpdateCustomerWithAddress() {
        customer1.setPhone("987654321");
        customerRepository.save(customer1);
        Optional<Customer> updatedCustomer = customerRepository.findById(customer1.getId());
        assertThat(updatedCustomer).isPresent();
        assertThat(updatedCustomer.get().getPhone()).isEqualTo("987654321");
    }

    @Test
    void shouldDeleteCustomer() {
        customerRepository.deleteById(customer1.getId());

        Optional<Customer> deletedCustomer = customerRepository.findById(customer1.getId());
        assertThat(deletedCustomer).isNotPresent();
    }

    @Test
    void shouldFindAddressByCustomerId() {
        List<Address> addresses = addressRepository.findByCustomer_Id(customer1.getId());
        assertThat(addresses).hasSize(2);
        assertThat(addresses.get(0).getStreet()).isEqualTo("Rua das Flores");
    }

}
