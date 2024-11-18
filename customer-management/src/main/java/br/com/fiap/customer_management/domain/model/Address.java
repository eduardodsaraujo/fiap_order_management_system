package br.com.fiap.customer_management.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String street;

    @Column(nullable = false, length = 10)
    private String number;

    @Column(length = 60)
    private String complement;

    @Column(nullable = false, length = 30)
    private String district;

    @Column(nullable = false, length = 30)
    private String city;

    @Column(nullable = false, length = 2)
    private String state;

    @Column(name = "postal_code", nullable = false, length = 8)
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "Customerid", nullable = false)
    private Customer customer;
}
