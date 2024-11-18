package br.com.fiap.customer_management.application.dto;

import br.com.fiap.customer_management.application.service.impl.AddressServiceImpl;
import br.com.fiap.customer_management.domain.model.Address;
import br.com.fiap.customer_management.domain.model.Customer;
import br.com.fiap.customer_management.domain.repository.AddressRepository;
import br.com.fiap.customer_management.domain.repository.CustomerRepository;
import br.com.fiap.customer_management.infrastructure.exception.AddressException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveAddress_ShouldReturnSavedAddressDTO() {
        Long customerId = 1L;
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setStreet("Street Name");
        Customer customer = new Customer();
        customer.setId(customerId);

        Address savedAddress = Address.builder().id(1L).street("Street Name").customer(customer).build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.save(any(Address.class))).thenReturn(savedAddress);

        AddressDTO result = addressService.saveAddress(customerId, addressRequestDTO);

        assertNotNull(result);
        assertEquals("Street Name", result.getStreet());
        assertEquals(customerId, result.getCustomerId());
        verify(customerRepository, times(1)).findById(customerId);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void findAllAddresses_ShouldReturnListOfAddressDTOs() {
        Long customerId = 1L;
        Address address = Address.builder().id(1L).street("Street Name").build();
        when(addressRepository.findByCustomer_Id(customerId)).thenReturn(Collections.singletonList(address));

        List<AddressDTO> result = addressService.findAllAddresses(customerId);

        assertEquals(1, result.size());
        assertEquals("Street Name", result.get(0).getStreet());
        verify(addressRepository, times(1)).findByCustomer_Id(customerId);
    }

    @Test
    void findAddressById_ShouldReturnAddressDTO_WhenAddressExists() {
        Long customerId = 1L;
        Long addressId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        Address address = Address.builder().id(addressId).customer(customer).street("Street Name").build();

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        AddressDTO result = addressService.findAddressById(customerId, addressId);

        assertNotNull(result);
        assertEquals("Street Name", result.getStreet());
        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    void findAddressById_ShouldThrowAddressException_WhenAddressNotFound() {
        Long customerId = 1L;
        Long addressId = 1L;

        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AddressException.class, () -> addressService.findAddressById(customerId, addressId));
        assertEquals("Address not found", exception.getMessage());
        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    void updateAddress_ShouldReturnUpdatedAddressDTO() {
        Long customerId = 1L;
        Long addressId = 1L;
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setStreet("New Street Name");

        Customer customer = new Customer();
        customer.setId(customerId);
        Address existingAddress = Address.builder().id(addressId).customer(customer).street("Old Street Name").build();

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(any(Address.class))).thenReturn(existingAddress);

        AddressDTO result = addressService.updateAddress(customerId, addressId, addressRequestDTO);

        assertNotNull(result);
        assertEquals("New Street Name", result.getStreet());
        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, times(1)).save(existingAddress);
    }

    @Test
    void deleteAddress_ShouldCallDelete_WhenAddressExists() {
        Long customerId = 1L;
        Long addressId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        Address address = Address.builder().id(addressId).customer(customer).build();

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        addressService.deleteAddress(customerId, addressId);

        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, times(1)).delete(address);
    }

    @Test
    void deleteAddress_ShouldThrowAddressException_WhenAddressNotFound() {
        Long customerId = 1L;
        Long addressId = 1L;

        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AddressException.class, () -> addressService.deleteAddress(customerId, addressId));
        assertEquals("Address not found", exception.getMessage());
        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, never()).delete(any(Address.class));
    }

}
