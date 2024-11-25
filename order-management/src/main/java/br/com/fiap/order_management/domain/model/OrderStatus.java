package br.com.fiap.order_management.domain.model;

public enum OrderStatus {

    OPEN,
    WAITING_PAYMENT,
    PAID,
    AWAITING_SHIPMENT,
    CANCELED,
    DELIVERED

}
