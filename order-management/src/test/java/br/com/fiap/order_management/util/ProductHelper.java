package br.com.fiap.order_management.util;

import br.com.fiap.order_management.domain.model.Product;
import br.com.fiap.order_management.domain.output.ProductOutput;
import br.com.fiap.order_management.infra.gateway.db.mongo.document.ProductDocument;

public class ProductHelper {

    public static Product createProduct() {
        return Product.builder()
                .id(1L)
                .code("IP16P")
                .code("IPhone 16 Pro")
                .weight(1)
                .price(8000)
                .build();
    }

    public static ProductDocument createProductDocument() {
        return ProductDocument.builder()
                .id(1L)
                .code("IP16P")
                .code("IPhone 16 Pro")
                .weight(1)
                .price(8000)
                .build();
    }

    public static ProductOutput createProductOutput() {
        return ProductOutput.builder()
                .id(1L)
                .code("IP16P")
                .code("IPhone 16 Pro")
                .weight(1)
                .price(8000)
                .build();
    }
}
