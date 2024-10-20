package br.com.fiap.fiapcustomermanagement.Customer.Management.service;

import br.com.fiap.fiapcustomermanagement.Customer.Management.dto.AddressDTO;
import br.com.fiap.fiapcustomermanagement.Customer.Management.dto.AddressRequestDTO;
import br.com.fiap.fiapcustomermanagement.Customer.Management.infra.exception.AddressException;
import br.com.fiap.fiapcustomermanagement.Customer.Management.model.Address;
import br.com.fiap.fiapcustomermanagement.Customer.Management.model.Customer;
import br.com.fiap.fiapcustomermanagement.Customer.Management.repository.AddressRepository;
import br.com.fiap.fiapcustomermanagement.Customer.Management.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    public AddressDTO saveAddress(Long customerId, AddressRequestDTO addressRequestDTO) {
        Customer customer = findCustomerById(customerId);
        Address address = mapToEntity(addressRequestDTO, customer);
        return mapToDTO(addressRepository.save(address));
    }
    public List<AddressDTO> findAllAddresses(Long customerId) {
        return addressRepository.findByCustomer_Id(customerId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AddressDTO findAddressById(Long customerId, Long addressId) {
        Address address = findAddressByIdAndCustomer(customerId, addressId);
        return mapToDTO(address);
    }
    public AddressDTO updateAddress(Long customerId, Long addressId, AddressRequestDTO addressRequestDTO) {
        Address existingAddress = findAddressByIdAndCustomer(customerId, addressId);
        updateAddressFields(existingAddress, addressRequestDTO);
        return mapToDTO(addressRepository.save(existingAddress));
    }
    public void deleteAddress(Long customerId, Long addressId) {
        Address address = findAddressByIdAndCustomer(customerId, addressId);
        addressRepository.delete(address);
    }
    private Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new AddressException("Customer not found"));
    }
    private Address findAddressByIdAndCustomer(Long customerId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressException("Address not found"));
        if (!address.getCustomer().getId().equals(customerId)) {
            throw new AddressException("Address does not belong to the customer");
        }
        return address;
    }

    // Atualizar os campos do endereço existente
    private void updateAddressFields(Address address, AddressRequestDTO addressRequestDTO) {
        address.setStreet(addressRequestDTO.getStreet());
        address.setNumber(addressRequestDTO.getNumber());
        address.setComplement(addressRequestDTO.getComplement());
        address.setDistrict(addressRequestDTO.getDistrict());
        address.setCity(addressRequestDTO.getCity());
        address.setState(addressRequestDTO.getState());
        address.setPostalCode(addressRequestDTO.getPostalCode());
    }
    private Address mapToEntity(AddressRequestDTO addressRequestDTO, Customer customer) {
        return Address.builder()
                .street(addressRequestDTO.getStreet())
                .number(addressRequestDTO.getNumber())
                .complement(addressRequestDTO.getComplement())
                .district(addressRequestDTO.getDistrict())
                .city(addressRequestDTO.getCity())
                .state(addressRequestDTO.getState())
                .postalCode(addressRequestDTO.getPostalCode())
                .customer(customer)
                .build();
    }
    private AddressDTO mapToDTO(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .district(address.getDistrict())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .customerId(address.getCustomer().getId())
                .build();
    }
}
