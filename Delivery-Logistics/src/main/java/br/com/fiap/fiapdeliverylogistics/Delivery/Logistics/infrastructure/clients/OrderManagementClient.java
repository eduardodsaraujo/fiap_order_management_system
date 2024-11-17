package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.infrastructure.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

@FeignClient(value = "order-management", path = "//api/order")
public interface OrderManagementClient {
    @PutMapping(value = "/{orderId}/delivered")
    public ResponseEntity<Void> updateDelivered(@PathVariable UUID orderId);
}
