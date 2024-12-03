package br.com.fiap.product_management.api.controller;

import br.com.fiap.product_management.api.controller.dto.ProductDto;
import br.com.fiap.product_management.application.input.CreateProductInput;
import br.com.fiap.product_management.application.input.UpdateProductInput;
import br.com.fiap.product_management.application.service.ProductService;
import br.com.fiap.product_management.domain.model.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Operations related to product management")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Create a new product", description = "Create a new product with the provided details")
    public ResponseEntity<ProductDto> create(@RequestBody @Valid CreateProductInput input) {
        Product product = productService.create(input);
        return ResponseEntity.ok(ProductDto.toDto(product));
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update product", description = "Update an existing product's details")
    public ResponseEntity<ProductDto> update(@PathVariable long productId, @RequestBody @Valid UpdateProductInput input) {
        Product product = productService.update(productId, input);
        return ResponseEntity.ok(ProductDto.toDto(product));
    }

    @PutMapping("/{productId}/enable")
    @Operation(summary = "Enable product", description = "Enable an existing product")
    public ResponseEntity<ProductDto> enable(@PathVariable long productId) {
        Product product = productService.enable(productId);
        return ResponseEntity.ok(ProductDto.toDto(product));
    }

    @PutMapping("/{productId}/disable")
    @Operation(summary = "Disable product", description = "Disable an existing product")
    public ResponseEntity<ProductDto> disable(@PathVariable long productId) {
        Product product = productService.disable(productId);
        return ResponseEntity.ok(ProductDto.toDto(product));
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get product by ID", description = "Retrieve product details by ID")
    public ResponseEntity<ProductDto> findById(@PathVariable long productId) {
        Product product = productService.findById(productId);
        return ResponseEntity.ok(ProductDto.toDto(product));
    }

    @GetMapping("/all/[{productsIds}]")
    @Operation(summary = "Get product by ID", description = "Retrieve all product by IDs")
    public ResponseEntity<List<ProductDto>> findAllById(@PathVariable List<Long> productsIds) {
        List<Product> products = productService.findAllById(productsIds);
        return ResponseEntity.ok(ProductDto.toListDto(products));
    }

    @GetMapping
    @Operation(summary = "Get all products by name", description = "Retrieve all products by name")
    public ResponseEntity<List<ProductDto>> findAllByName(@RequestParam String name, Pageable pageable) {
        Page<Product> products = productService.findAllByName(name, pageable);
        return ResponseEntity.ok(ProductDto.toListDto(products.getContent()));
    }

}
