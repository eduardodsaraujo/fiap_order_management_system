package br.com.fiap.fiapproductmanagement.repository;

import br.com.fiap.product_management.domain.model.Product;
import br.com.fiap.product_management.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTest {


    private final ProductRepository productRepository;

    public ProductRepositoryTest(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        Product product1 = new Product();
        product1.setCode("P001");
        product1.setName("Test Product One");
        product1.setDescription("Description One");
        product1.setCategory("Category One");
        product1.setManufacturer("Manufacturer One");
        product1.setPrice(100.0);
        product1.setStockQuantity(50);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setCode("P002");
        product2.setName("Test Product Two");
        product2.setDescription("Description Two");
        product2.setCategory("Category Two");
        product2.setManufacturer("Manufacturer Two");
        product2.setPrice(200.0);
        product2.setStockQuantity(30);
        productRepository.save(product2);
    }

    @Test
    void findAllByNameLike_shouldReturnMatchingProducts() {
        String namePattern = "Test%";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> result = productRepository.findAllByNameLike(namePattern, pageable);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Test Product One");
        assertThat(result.getContent().get(1).getName()).isEqualTo("Test Product Two");
    }

    @Test
    void findAllByNameLike_shouldReturnEmptyPageWhenNoMatch() {
        String namePattern = "NonExistent%";
        Pageable pageable = PageRequest.of(0, 10);


        Page<Product> result = productRepository.findAllByNameLike(namePattern, pageable);
        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getContent()).isEmpty();
    }
}
