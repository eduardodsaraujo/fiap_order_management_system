package br.com.fiap.productimport.job;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductImport {

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

}
