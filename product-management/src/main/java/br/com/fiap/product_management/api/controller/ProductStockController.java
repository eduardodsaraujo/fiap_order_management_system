package br.com.fiap.product_management.api.controller;

import br.com.fiap.product_management.application.input.UpdateStockInput;
import br.com.fiap.product_management.application.service.ProductStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/stock")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Operations related to product management")
public class ProductStockController {

    private final ProductStockService productStockService;

    @PutMapping("/increase")
    @Operation(summary = "Increase product stock", description = "Increase stock of an existing product")
    public ResponseEntity<Void> increase(@RequestBody UpdateStockInput input) {
        productStockService.increase(input);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/decrease")
    @Operation(summary = "Decrease product stock", description = "Decrease stock of an existing product")
    public ResponseEntity<Void> decrease(@RequestBody UpdateStockInput input) {
        productStockService.decrease(input);
        return ResponseEntity.ok().build();
    }

}
