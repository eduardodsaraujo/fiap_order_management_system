package br.com.fiap.product_management.domain.model;

import br.com.fiap.product_management.infra.exception.ProductException;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    private String name;
    private String description;
    private String category;
    private String manufacturer;
    private boolean enable;
    private double price;
    private double weight;
    @Column(name = "stock_quantity")
    private double stockQuantity;

    public void enable() {
        this.enable = true;
    }

    public void disable() {
        this.enable = false;
    }

    public void increase(double quantity) {
        stockQuantity += quantity;
    }

    public void decrease(double quantity) {
        if (quantity > stockQuantity) {
            throw new ProductException("Insufficient product stock");
        }
        stockQuantity -= quantity;
    }

}
