package br.com.fiap.customer_management.application.service.impl;


import br.com.fiap.customer_management.application.AddressDTO;
import br.com.fiap.customer_management.application.AddressRequestDTO;
import br.com.fiap.customer_management.application.service.impl.AddressServiceImpl;
import br.com.fiap.customer_management.domain.model.Address;
import br.com.fiap.customer_management.domain.model.Customer;
import br.com.fiap.customer_management.domain.repository.AddressRepository;
import br.com.fiap.customer_management.domain.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static br.com.fiap.customer_management.utils.AddressHelper.createAddress;
import static br.com.fiap.customer_management.utils.AddressHelper.createAddressRequestDTO;
import static br.com.fiap.customer_management.utils.CustomerHelper.createCustomer;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class AddressServiceImplTestIT {

    @Autowired
    private AddressServiceImpl addressService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = createCustomer();
        customer = customerRepository.save(customer);
    }

    @Test
    void shouldReturnSavedAddressDTO() {
        AddressRequestDTO addressRequestDTO = createAddressRequestDTO();
        AddressDTO result = addressService.saveAddress(customer.getId(), addressRequestDTO);

        assertNotNull(result);
        assertEquals("Rua das Flores", result.getStreet());
        assertEquals(customer.getId(), result.getCustomerId());

        Optional<Address> address = addressRepository.findById(result.getId());
        assertTrue(address.isPresent());
        assertEquals("Rua das Flores", address.get().getStreet());
    }

    @Test
    void shouldReturnListOfAddressDTOs() {
        Address address = createAddress();
        address.setCustomer(customer);
        addressRepository.save(address);

        List<AddressDTO> result = addressService.findAllAddresses(customer.getId());

        assertEquals(1, result.size());
        assertEquals("Rua das Flores", result.get(0).getStreet());
    }

    @Test
    void shouldReturnAddressDTO_WhenAddressExists() {
        Address address = createAddress();
        address.setCustomer(customer);
        addressRepository.save(address);

        AddressDTO result = addressService.findAddressById(customer.getId(), address.getId());

        assertNotNull(result);
        assertEquals("Rua das Flores", result.getStreet());
    }

    @Test
    void shouldRemoveAddress_WhenAddressExists() {
        Address address = createAddress();
        address.setCustomer(customer);
        addressRepository.save(address);

        addressService.deleteAddress(customer.getId(), address.getId());

        Optional<Address> deletedAddress = addressRepository.findById(address.getId());
        assertFalse(deletedAddress.isPresent());
    }
}
