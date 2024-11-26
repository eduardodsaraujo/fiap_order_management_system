package br.com.fiap.customer_management.application.dto;


import br.com.fiap.customer_management.application.dto.AddressDTO;
import br.com.fiap.customer_management.application.dto.AddressRequestDTO;
import br.com.fiap.customer_management.application.service.impl.AddressServiceImpl;
import br.com.fiap.customer_management.domain.model.Address;
import br.com.fiap.customer_management.domain.model.Customer;
import br.com.fiap.customer_management.domain.repository.AddressRepository;
import br.com.fiap.customer_management.domain.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
        customer = Customer.builder().name("John Doe").build();
        customerRepository.save(customer);
    }

    @Test
    void saveAddress_ShouldReturnSavedAddressDTO() {
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setStreet("Street Name");

        AddressDTO result = addressService.saveAddress(customer.getId(), addressRequestDTO);

        assertNotNull(result);
        assertEquals("Street Name", result.getStreet());
        assertEquals(customer.getId(), result.getCustomerId());

        Optional<Address> address = addressRepository.findById(result.getId());
        assertTrue(address.isPresent());
        assertEquals("Street Name", address.get().getStreet());
    }

    @Test
    void findAllAddresses_ShouldReturnListOfAddressDTOs() {
        Address address = Address.builder().street("Street Name").customer(customer).build();
        addressRepository.save(address);

        List<AddressDTO> result = addressService.findAllAddresses(customer.getId());

        assertEquals(1, result.size());
        assertEquals("Street Name", result.get(0).getStreet());
    }

    @Test
    void findAddressById_ShouldReturnAddressDTO_WhenAddressExists() {
        Address address = Address.builder().street("Street Name").customer(customer).build();
        addressRepository.save(address);

        AddressDTO result = addressService.findAddressById(customer.getId(), address.getId());

        assertNotNull(result);
        assertEquals("Street Name", result.getStreet());
    }

    @Test
    void deleteAddress_ShouldRemoveAddress_WhenAddressExists() {
        Address address = Address.builder().street("Street Name").customer(customer).build();
        addressRepository.save(address);

        addressService.deleteAddress(customer.getId(), address.getId());

        Optional<Address> deletedAddress = addressRepository.findById(address.getId());
        assertFalse(deletedAddress.isPresent());
    }
}
