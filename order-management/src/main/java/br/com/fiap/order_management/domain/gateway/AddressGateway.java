package br.com.fiap.order_management.domain.gateway;

import br.com.fiap.order_management.domain.model.DeliveryAddress;

public interface AddressGateway {

    DeliveryAddress findAddressByCustomerIdAndAddressId(long customerId, long addressId);

}
