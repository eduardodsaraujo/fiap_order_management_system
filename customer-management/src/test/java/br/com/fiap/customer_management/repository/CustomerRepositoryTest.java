package br.com.fiap.customer_management.repository;

import br.com.fiap.customer_management.domain.model.Address;
import br.com.fiap.customer_management.domain.model.Customer;
import br.com.fiap.customer_management.domain.repository.AddressRepository;
import br.com.fiap.customer_management.domain.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhone("123456789");

        Address address = new Address();
        address.setStreet("123 Main St");
        address.setNumber("101");
        address.setDistrict("Downtown");
        address.setCity("Metropolis");
        address.setState("SP");
        address.setPostalCode("12345678");
        address.setCustomer(customer);

        customer.setAddresses(List.of(address));

        customerRepository.save(customer);
    }

    @Test
    void testSaveCustomerWithAddress() {
        Customer savedCustomer = customerRepository.findById(customer.getId()).orElse(null);
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getName()).isEqualTo("John Doe");
        List<Address> addresses = savedCustomer.getAddresses();
        assertThat(addresses).isNotEmpty();
        assertThat(addresses.get(0).getStreet()).isEqualTo("123 Main St");
    }

    @Test
    void testFindCustomerById() {
        Customer foundCustomer = customerRepository.findById(customer.getId()).orElse(null);
        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer.getName()).isEqualTo("John Doe");
    }

    @Test
    void testFindAddressByCustomerId() {
        List<Address> addresses = addressRepository.findByCustomer_Id(customer.getId());
        assertThat(addresses).isNotEmpty();
        assertThat(addresses.get(0).getStreet()).isEqualTo("123 Main St");
    }

    @Test
    void testDeleteCustomerWithAddress() {
        customerRepository.delete(customer);

        Customer deletedCustomer = customerRepository.findById(customer.getId()).orElse(null);
        assertThat(deletedCustomer).isNull();

        List<Address> addresses = addressRepository.findByCustomer_Id(customer.getId());
        assertThat(addresses).isEmpty();
    }
}
