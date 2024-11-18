package br.com.fiap.customer_management.domain.repository;

import br.com.fiap.customer_management.domain.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByCustomer_Id(Long customerId);
}
