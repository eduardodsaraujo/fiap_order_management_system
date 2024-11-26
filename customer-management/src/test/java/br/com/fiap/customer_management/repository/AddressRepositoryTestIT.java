package br.com.fiap.customer_management.repository;

import br.com.fiap.customer_management.domain.model.Address;
import br.com.fiap.customer_management.domain.repository.AddressRepository;
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
public class AddressRepositoryTestIT {

    @Autowired
    private AddressRepository addressRepository;

    private Address address1;
    private Address address2;

    @BeforeEach
    void setUp() {
        address1 = new Address();
        address1.setStreet("Street 1");
        address1.setNumber("123");
        address1.setCity("City 1");
        address1.setState("State 1");
        address1.setPostalCode("12345");

        address2 = new Address();
        address2.setStreet("Street 2");
        address2.setNumber("456");
        address2.setCity("City 2");
        address2.setState("State 2");
        address2.setPostalCode("67890");
        addressRepository.save(address1);
        addressRepository.save(address2);
    }

    @Test
    void testSaveAddress() {
        Address newAddress = new Address();
        newAddress.setStreet("New Street");
        newAddress.setNumber("789");
        newAddress.setCity("New City");
        newAddress.setState("New State");
        newAddress.setPostalCode("11111");
        Address savedAddress = addressRepository.save(newAddress);
        assertThat(savedAddress.getId()).isNotNull();
        assertThat(savedAddress.getStreet()).isEqualTo("New Street");
        assertThat(savedAddress.getPostalCode()).isEqualTo("11111");
    }

    @Test
    void testFindAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        assertThat(addresses).isNotEmpty();
        assertThat(addresses).hasSize(2);
    }

    @Test
    void testFindById() {
        Optional<Address> foundAddress = addressRepository.findById(address1.getId());
        assertThat(foundAddress).isPresent();
        assertThat(foundAddress.get().getStreet()).isEqualTo("Street 1");
    }

    @Test
    void testFindByCustomerId() {
        Long customerId = 1L;

        List<Address> addresses = addressRepository.findByCustomer_Id(customerId);
        assertThat(addresses).isNotEmpty();
    }

    @Test
    void testDeleteAddress() {
        addressRepository.delete(address1);

        Optional<Address> deletedAddress = addressRepository.findById(address1.getId());

        assertThat(deletedAddress).isNotPresent();
    }


}
