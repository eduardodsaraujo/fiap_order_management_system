package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model;

public enum DeliveryStatus {
    PENDING, // Pendente
    WAITING_FOR_PICKUP, // Aguardando a Coleta
    ON_THE_WAY, // A Caminho
    DELIVERED // Entregue
}
