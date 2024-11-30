package br.com.fiap.customer_management.application.service.impl;

import br.com.fiap.customer_management.application.AddressDTO;
import br.com.fiap.customer_management.application.AddressRequestDTO;
import br.com.fiap.customer_management.domain.model.Address;
import br.com.fiap.customer_management.domain.model.Customer;
import br.com.fiap.customer_management.domain.repository.AddressRepository;
import br.com.fiap.customer_management.domain.repository.CustomerRepository;
import br.com.fiap.customer_management.infrastructure.exception.AddressException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static br.com.fiap.customer_management.utils.AddressHelper.createAddress;
import static br.com.fiap.customer_management.utils.AddressHelper.createAddressRequestDTO;
import static br.com.fiap.customer_management.utils.CustomerHelper.createCustomer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AddressServiceImplTest {

    private AddressServiceImpl addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CustomerRepository customerRepository;

    private AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        addressService = new AddressServiceImpl(addressRepository, customerRepository);
    }

    @Test
    void shouldSaveAddressSuccessfully() {
        Long customerId = 1L;
        AddressRequestDTO addressRequestDTO = createAddressRequestDTO();
        Customer customer = createCustomer();
        customer.setId(customerId);
        Address address = createAddress(1L, customer);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressDTO savedAddress = addressService.saveAddress(customerId, addressRequestDTO);

        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getId()).isEqualTo(1L);
        assertThat(savedAddress.getStreet()).isEqualTo("Rua das Flores");
        verify(customerRepository, times(1)).findById(customerId);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void shouldFindAllAddressesSuccessfully() {
        Long customerId = 1L;
        Customer customer = createCustomer();
        customer.setId(customerId);
        List<Address> addresses = List.of(createAddress(1L, customer), createAddress(2L, customer));

        when(addressRepository.findByCustomer_Id(customerId)).thenReturn(addresses);

        List<AddressDTO> result = addressService.findAllAddresses(customerId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        verify(addressRepository, times(1)).findByCustomer_Id(customerId);
    }

    @Test
    void shouldFindAddressByIdSuccessfully() {
        Long customerId = 1L;
        Long addressId = 2L;
        Customer customer = createCustomer();
        customer.setId(customerId);
        Address address = createAddress(addressId, customer);

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        AddressDTO result = addressService.findAddressById(customerId, addressId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(addressId);
        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFound() {
        Long customerId = 1L;
        Long addressId = 2L;

        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> addressService.findAddressById(customerId, addressId))
                .isInstanceOf(AddressException.class)
                .hasMessage("Address not found");

        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    void shouldUpdateAddressSuccessfully() {
        Long customerId = 1L;
        Long addressId = 2L;
        Customer customer = createCustomer();
        customer.setId(customerId);
        Address existingAddress = createAddress(addressId, customer);
        AddressRequestDTO updatedRequest = AddressRequestDTO.builder()
                .street("Rua Nova")
                .number("999")
                .district("Centro")
                .city("Rio de Janeiro")
                .state("RJ")
                .postalCode("20000000")
                .build();

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(any(Address.class))).thenReturn(existingAddress);

        AddressDTO updatedAddress = addressService.updateAddress(customerId, addressId, updatedRequest);

        assertThat(updatedAddress).isNotNull();
        assertThat(updatedAddress.getStreet()).isEqualTo("Rua Nova");
        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, times(1)).save(existingAddress);
    }

    @Test
    void shouldDeleteAddressSuccessfully() {
        Long customerId = 1L;
        Long addressId = 2L;
        Customer customer = createCustomer();
        customer.setId(customerId);
        Address address = createAddress(addressId, customer);

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        addressService.deleteAddress(customerId, addressId);

        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, times(1)).delete(address);
    }



}
