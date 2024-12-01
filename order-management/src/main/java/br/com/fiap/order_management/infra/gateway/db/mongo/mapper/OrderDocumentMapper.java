package br.com.fiap.order_management.infra.gateway.db.mongo.mapper;

import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.infra.gateway.db.mongo.document.OrderDocument;

public class OrderDocumentMapper {

    public static Order toDomain(OrderDocument orderDocument) {
        return Order.builder()
                .id(orderDocument.getId())
                .orderDate(orderDocument.getOrderDate())
                .status(orderDocument.getStatus())
                .itemTotal(orderDocument.getItemTotal())
                .shippingValue(orderDocument.getShippingValue())
                .total(orderDocument.getTotal())
                .totalWeight(orderDocument.getTotalWeight())
                .customer(CustomerDocumentMapper.toDomain(orderDocument.getCustomer()))
                .deliveryAddress(DeliveryAddressDocumentMapper.toDomain(orderDocument.getDeliveryAddress()))
                .payment(PaymentDocumentMapper.toDomain(orderDocument.getPayment()))
                .items(orderDocument.getItems().stream().map(OrderItemDocumentMapper::toDomain).toList())
                .build();
    }

    public static OrderDocument toDocument(Order order)  {
        return OrderDocument.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .itemTotal(order.getItemTotal())
                .shippingValue(order.getShippingValue())
                .total(order.getTotal())
                .totalWeight(order.getTotalWeight())
                .customer(CustomerDocumentMapper.toDocument(order.getCustomer()))
                .deliveryAddress(DeliveryAddressDocumentMapper.toDocument(order.getDeliveryAddress()))
                .payment(PaymentDocumentMapper.toDocument(order.getPayment()))
                .items(order.getItems().stream().map(OrderItemDocumentMapper::toDocument).toList())
                .build();
    }

}
