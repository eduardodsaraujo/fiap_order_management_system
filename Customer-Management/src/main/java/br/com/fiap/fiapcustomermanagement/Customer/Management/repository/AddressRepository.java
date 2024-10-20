package br.com.fiap.fiapcustomermanagement.Customer.Management.repository;

import br.com.fiap.fiapcustomermanagement.Customer.Management.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByCustomer_Id(Long customerId);
}
