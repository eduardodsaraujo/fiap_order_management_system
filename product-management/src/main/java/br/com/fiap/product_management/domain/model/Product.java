package br.com.fiap.product_management.domain.model;

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
    @Column(name = "stock_quantity")
    private double stockQuantity;

    public void enable() {
        this.enable = true;
    }

    public void disable() {
        this.enable = false;
    }

}
