package br.com.fiap.customer_management.domain.repository;

import br.com.fiap.customer_management.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
