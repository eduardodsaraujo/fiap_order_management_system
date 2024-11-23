package br.com.fiap.order_management.domain.gateway;

import br.com.fiap.order_management.domain.model.Customer;

public interface CustomerGateway {

    Customer findById(long customerId);

}
