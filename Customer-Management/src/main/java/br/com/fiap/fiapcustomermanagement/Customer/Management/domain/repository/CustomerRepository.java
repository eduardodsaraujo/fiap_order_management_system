package br.com.fiap.fiapcustomermanagement.Customer.Management.domain.repository;

import br.com.fiap.fiapcustomermanagement.Customer.Management.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
