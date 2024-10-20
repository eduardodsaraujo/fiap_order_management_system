package br.com.fiap.fiapcustomermanagement.Customer.Management.service;

import br.com.fiap.fiapcustomermanagement.Customer.Management.dto.AddressDTO;
import br.com.fiap.fiapcustomermanagement.Customer.Management.dto.CustomerDTO;
import br.com.fiap.fiapcustomermanagement.Customer.Management.dto.CustomerRequestDTO;
import br.com.fiap.fiapcustomermanagement.Customer.Management.infra.exception.CustomerException;
import br.com.fiap.fiapcustomermanagement.Customer.Management.model.Customer;
import br.com.fiap.fiapcustomermanagement.Customer.Management.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerService {

    private CustomerRepository customerRepository;

    public List<CustomerDTO> findAll() {
        return customerRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO saveCustomer(CustomerRequestDTO customerRequestDTO) {
        Customer customer = toEntity(customerRequestDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return toDTO(savedCustomer);
    }

    public CustomerDTO findById(Long id) {
        Customer customer = findCustomerById(id);
        return toDTO(customer);
    }

    public CustomerDTO updateCustomer(Long id, CustomerRequestDTO customerRequestDTO) {
        Customer customer = findCustomerById(id);
        Customer newCustomer = toEntity(customerRequestDTO);
        newCustomer.setId(customer.getId());
        Customer savedCustomer = customerRepository.save(customer);
        return toDTO(customer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = findCustomerById(id);
        customerRepository.deleteById(customer.getId());
    }


    private Customer toEntity(CustomerRequestDTO customerRequestDTO) {
        Customer customer = new Customer();
        customer.setName(customerRequestDTO.getName());
        customer.setEmail(customerRequestDTO.getEmail());
        customer.setPhone(customerRequestDTO.getPhone());
        return customer;
    }

    private CustomerDTO toDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhone(customer.getPhone());

        if(customer.getAddresses() != null)
            customerDTO.setAddresses(
                    customer.getAddresses().stream()
                            .map(address -> {
                                AddressDTO addressDTO = new AddressDTO();
                                addressDTO.setId(address.getId());
                                addressDTO.setStreet(address.getStreet());
                                addressDTO.setNumber(address.getNumber());
                                addressDTO.setComplement(address.getComplement());
                                addressDTO.setDistrict(address.getDistrict());
                                addressDTO.setCity(address.getCity());
                                addressDTO.setState(address.getState());
                                addressDTO.setPostalCode(address.getPostalCode());
                                return addressDTO;
                            }).collect(Collectors.toList())
            );
        return customerDTO;
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerException("Customer not found"));
    }

}
