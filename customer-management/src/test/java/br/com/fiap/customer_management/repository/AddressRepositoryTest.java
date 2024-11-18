package br.com.fiap.customer_management.repository;


import br.com.fiap.customer_management.domain.model.Address;
import br.com.fiap.customer_management.domain.repository.AddressRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Test
    void shouldFindAddressByCustomerId() {
        Address address = new Address();
        addressRepository.save(address);

        List<Address> addresses = addressRepository.findByCustomer_Id(1L);
        assertFalse(addresses.isEmpty());
    }
}
