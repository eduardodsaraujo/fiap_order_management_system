package br.com.fiap.order_management.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;
@Getter
@Builder
@Document
public class OrderItem {

    @Id
    private UUID id;
    private double quantity;
    private Product product;

}
