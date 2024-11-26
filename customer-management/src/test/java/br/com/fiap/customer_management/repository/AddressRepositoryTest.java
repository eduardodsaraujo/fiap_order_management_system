package br.com.fiap.customer_management.repository;


import br.com.fiap.customer_management.domain.model.Address;
import br.com.fiap.customer_management.domain.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AddressRepositoryTest {

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
    void whenFindByCustomerId_thenReturnAddresses() {
        Long customerId = 1L;
        List<Address> foundAddresses = addressRepository.findByCustomer_Id(customerId);
        assertThat(foundAddresses).isNotEmpty();
    }

    @Test
    void whenNoAddressesForCustomer_thenReturnEmptyList() {
        Long customerId = 999L;
        List<Address> foundAddresses = addressRepository.findByCustomer_Id(customerId);
        assertThat(foundAddresses).isEmpty();
    }

    @Test
    void whenSaveAddress_thenAddressShouldBeSaved() {
        Address newAddress = new Address();
        newAddress.setStreet("New Street");
        newAddress.setNumber("789");
        newAddress.setCity("New City");
        newAddress.setState("New State");
        newAddress.setPostalCode("00000");

        Address savedAddress = addressRepository.save(newAddress);
        assertThat(savedAddress.getId()).isNotNull();
        assertThat(savedAddress.getStreet()).isEqualTo("New Street");
    }

    @Test
    void whenDeleteAddress_thenAddressShouldBeDeleted() {
        addressRepository.delete(address1);
        List<Address> addresses = addressRepository.findByCustomer_Id(1L);
        assertThat(addresses).doesNotContain(address1);
    }
}
