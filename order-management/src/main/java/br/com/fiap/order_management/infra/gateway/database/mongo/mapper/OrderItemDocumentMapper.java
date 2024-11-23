package br.com.fiap.order_management.infra.gateway.database.mongo.mapper;

import br.com.fiap.order_management.domain.model.OrderItem;
import br.com.fiap.order_management.infra.gateway.database.mongo.document.OrderItemDocument;

public class OrderItemDocumentMapper {

    public static OrderItemDocument toDocument(OrderItem orderItem)  {
        return OrderItemDocument.builder()
                .id(orderItem.getId())
                .quantity(orderItem.getQuantity())
                .product(ProductDocumentMapper.toDocument(orderItem.getProduct()))
                .build();
    }

    public static OrderItem toDomain(OrderItemDocument orderItemDocument)  {
        return OrderItem.builder()
                .id(orderItemDocument.getId())
                .quantity(orderItemDocument.getQuantity())
                .product(ProductDocumentMapper.toDomain(orderItemDocument.getProduct()))
                .build();
    }

}
