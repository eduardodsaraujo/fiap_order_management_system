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
public class AddressRepositoryTestIT {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AddressRepository addressRepository;

    private Customer customer1;
    private Address address1;
    private Address address2;

    @BeforeEach
    void setUp() {
        addressRepository.deleteAll();
        customerRepository.deleteAll();

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

    }


    @Test
    void shouldSaveAddress() {
        var customer1 = new Customer();
        customer1.setName("Customer 2");
        customer1.setEmail("customer2@example.com");
        customer1.setPhone("21123456789");

        customer1 = customerRepository.save(customer1);
        var address1 = createAddress();
        address1.setCustomer(customer1);
        Address savedAddress = addressRepository.save(address1);

        assertThat(savedAddress.getId()).isNotNull();
        assertThat(savedAddress.getStreet()).isEqualTo("Rua das Flores");
        assertThat(savedAddress.getPostalCode()).isEqualTo("01001000");
    }

    @Test
    void shouldFindAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        assertThat(addresses).isNotEmpty();
        assertThat(addresses).hasSize(2);
    }

    @Test
    void shouldFindById() {
        Optional<Address> foundAddress = addressRepository.findById(address1.getId());
        assertThat(foundAddress).isPresent();
        assertThat(foundAddress.get().getStreet()).isEqualTo("Rua das Flores");
    }

    @Test
    void shouldFindByCustomerId() {

        List<Address> addresses = addressRepository.findByCustomer_Id(customer1.getId());
        assertThat(addresses).isNotEmpty();
    }

    @Test
    void shouldDeleteAddress() {
        addressRepository.deleteById(address1.getId());

        Optional<Address> deletedAddress = addressRepository.findById(address1.getId());

        assertThat(deletedAddress).isNotPresent();
    }


}
