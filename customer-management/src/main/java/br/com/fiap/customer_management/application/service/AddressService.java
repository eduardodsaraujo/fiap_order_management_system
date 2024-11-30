package br.com.fiap.customer_management.application.service;

import br.com.fiap.customer_management.application.AddressDTO;
import br.com.fiap.customer_management.application.AddressRequestDTO;

import java.util.List;

public interface AddressService {

    public AddressDTO saveAddress(Long customerId, AddressRequestDTO addressRequestDTO);

    public List<AddressDTO> findAllAddresses(Long customerId);

    public AddressDTO findAddressById(Long customerId, Long addressId);

    public AddressDTO updateAddress(Long customerId, Long addressId, AddressRequestDTO addressRequestDTO);

    public void deleteAddress(Long customerId, Long addressId);
}