package br.com.fiap.order_management.infra.gateway.integration;

import br.com.fiap.order_management.domain.gateway.AddressGateway;
import br.com.fiap.order_management.domain.model.DeliveryAddress;
import br.com.fiap.order_management.infra.gateway.integration.gateway.AddressWsGateway;
import br.com.fiap.order_management.infra.gateway.integration.dto.customer.AddressDto;
import br.com.fiap.order_management.infra.gateway.integration.mapper.AddressDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressIntegrationGateway implements AddressGateway {

    private final AddressWsGateway addressWsGateway;

    @Override
    public DeliveryAddress findAddressByCustomerIdAndAddressId(long customerId, long addressId) {
        AddressDto addressDto = addressWsGateway.findByCustomerIdAndAddressId(customerId, addressId);

        return AddressDtoMapper.toDomain(addressDto);
    }

}
