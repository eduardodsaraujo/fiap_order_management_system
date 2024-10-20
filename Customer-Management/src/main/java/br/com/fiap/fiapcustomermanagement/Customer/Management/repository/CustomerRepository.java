package br.com.fiap.fiapcustomermanagement.Customer.Management.repository;

import br.com.fiap.fiapcustomermanagement.Customer.Management.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
