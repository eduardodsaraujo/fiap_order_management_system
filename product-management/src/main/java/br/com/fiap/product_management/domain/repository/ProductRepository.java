package br.com.fiap.product_management.domain.repository;

import br.com.fiap.product_management.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByNameLike(String name, Pageable pageable);

}
