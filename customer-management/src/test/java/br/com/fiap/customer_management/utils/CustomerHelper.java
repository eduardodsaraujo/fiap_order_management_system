package br.com.fiap.customer_management.utils;

import br.com.fiap.customer_management.application.CustomerDTO;
import br.com.fiap.customer_management.application.CustomerRequestDTO;
import br.com.fiap.customer_management.domain.model.Customer;

public class CustomerHelper {


    public static Customer createCustomer() {
        var customer = new Customer();
        customer.setId(1L);
        customer.setName("Eduardo");
        customer.setEmail("eduardo@gmail.com");
        customer.setPhone("21984506545");

        return customer;
    }
    public static CustomerRequestDTO createCustomerRequestDTO() {
        var customer = new CustomerRequestDTO();
        customer.setName("Eduardo");
        customer.setEmail("eduardo@gmail.com");
        customer.setPhone("21984506545");

        return customer;
    }

    public static CustomerDTO createCustomerDTO(Long id) {
        var customer = new CustomerDTO();
        customer.setId(id);
        customer.setName("Eduardo");
        customer.setEmail("eduardo@gmail.com");
        customer.setPhone("21984506545");

        return customer;
    }
}
