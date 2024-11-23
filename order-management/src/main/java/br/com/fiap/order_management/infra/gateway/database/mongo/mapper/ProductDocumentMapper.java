package br.com.fiap.order_management.infra.gateway.database.mongo.mapper;

import br.com.fiap.order_management.domain.model.Product;
import br.com.fiap.order_management.infra.gateway.database.mongo.document.ProductDocument;

public class ProductDocumentMapper {

    public static ProductDocument toDocument(Product product) {
        return ProductDocument.builder()
                .id(product.getId())
                .code(product.getCode())
                .description(product.getDescription())
                .price(product.getPrice())
                .weight(product.getWeight())
                .build();
    }

    public static Product toDomain(ProductDocument productDocument) {
        return Product.builder()
                .id(productDocument.getId())
                .code(productDocument.getCode())
                .description(productDocument.getDescription())
                .price(productDocument.getPrice())
                .weight(productDocument.getWeight())
                .build();
    }

}
