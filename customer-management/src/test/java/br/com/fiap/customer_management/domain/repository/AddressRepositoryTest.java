package br.com.fiap.customer_management.domain.repository;

import br.com.fiap.customer_management.domain.model.Address;
import br.com.fiap.customer_management.domain.repository.AddressRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static br.com.fiap.customer_management.utils.AddressHelper.createAddress;
import static br.com.fiap.customer_management.utils.CustomerHelper.createCustomer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AddressRepositoryTest {

    @Mock
    private AddressRepository addressRepository;

    private AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void shouldSaveAddress() {
        // Arrange
        var address = createAddress();
        var customer = createCustomer();
        address.setCustomer(customer);

        when(addressRepository.save(any(Address.class))).thenReturn(address);

        // Act
        var savedAddress = addressRepository.save(address);

        // Assert
        assertThat(savedAddress)
                .isNotNull()
                .isEqualTo(address);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void shouldFindAddressById() {
        // Arrange
        Long id = 1L;
        var address = createAddress();
        var customer = createCustomer();
        address.setId(id);
        address.setCustomer(customer);

        when(addressRepository.findById(any(Long.class))).thenReturn(Optional.of(address));

        // Act
        var foundAddressOptional = addressRepository.findById(id);

        // Assert
        assertThat(foundAddressOptional).isPresent().containsSame(address);
        foundAddressOptional.ifPresent(foundAddress -> {
            assertThat(foundAddress.getId()).isEqualTo(address.getId());
            assertThat(foundAddress.getStreet()).isEqualTo(address.getStreet());
            assertThat(foundAddress.getCity()).isEqualTo(address.getCity());
        });
        verify(addressRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void shouldFindAddressesByCustomerId() {
        // Arrange
        Long customerId = 1L;
        var customer = createCustomer();
        var address1 = createAddress();
        address1.setId(1L);
        address1.setCustomer(customer);

        var address2 = createAddress();
        address2.setId(2L);
        address2.setCustomer(customer);

        var addresses = List.of(address1, address2);
        when(addressRepository.findByCustomer_Id(any(Long.class))).thenReturn(addresses);

        // Act
        var foundAddresses = addressRepository.findByCustomer_Id(customerId);

        // Assert
        assertThat(foundAddresses)
                .hasSize(2)
                .containsExactlyInAnyOrder(address1, address2);
        verify(addressRepository, times(1)).findByCustomer_Id(any(Long.class));
    }

    @Test
    void shouldDeleteAddressById() {
        // Arrange
        Long id = 1L;
        doNothing().when(addressRepository).deleteById(any(Long.class));

        // Act
        addressRepository.deleteById(id);

        // Assert
        verify(addressRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldFindAllAddresses() {
        // Arrange
        var address1 = createAddress();
        address1.setId(1L);

        var address2 = createAddress();
        address2.setId(2L);

        var addresses = Arrays.asList(address1, address2);
        when(addressRepository.findAll()).thenReturn(addresses);

        // Act
        var foundAddresses = addressRepository.findAll();

        // Assert
        assertThat(foundAddresses)
                .hasSize(2)
                .containsExactlyInAnyOrder(address1, address2);
        verify(addressRepository, times(1)).findAll();
    }
}
