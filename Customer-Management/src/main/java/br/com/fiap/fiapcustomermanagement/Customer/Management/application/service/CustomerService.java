package br.com.fiap.fiapcustomermanagement.Customer.Management.application.service;

import br.com.fiap.fiapcustomermanagement.Customer.Management.application.service.dto.CustomerDTO;
import br.com.fiap.fiapcustomermanagement.Customer.Management.application.service.dto.CustomerRequestDTO;

import java.util.List;

public interface CustomerService {

    public List<CustomerDTO> findAll();
    public CustomerDTO saveCustomer(CustomerRequestDTO customerRequestDTO);
    public CustomerDTO findById(Long id);
    public CustomerDTO updateCustomer(Long id, CustomerRequestDTO customerRequestDTO);
    public void deleteCustomer(Long id);

}