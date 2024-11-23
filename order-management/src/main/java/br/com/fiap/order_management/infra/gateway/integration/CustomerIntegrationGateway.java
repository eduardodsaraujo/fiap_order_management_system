package br.com.fiap.order_management.infra.gateway.integration;

import br.com.fiap.order_management.domain.gateway.CustomerGateway;
import br.com.fiap.order_management.domain.model.Customer;
import br.com.fiap.order_management.infra.gateway.integration.dto.customer.CustomerDto;
import br.com.fiap.order_management.infra.gateway.integration.gateway.CustomerWsGateway;
import br.com.fiap.order_management.infra.gateway.integration.mapper.CustomerDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerIntegrationGateway implements CustomerGateway {

    private final CustomerWsGateway customerWsGateway;

    @Override
    public Customer findById(long customerId) {
        CustomerDto customerDto = customerWsGateway.findById(customerId);
        return CustomerDtoMapper.toDomain(customerDto);
    }

}
