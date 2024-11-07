package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.controller;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.DeliveryPersonService;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.DeliveryPersonRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deliveryperson")
@AllArgsConstructor
@Tag(name = "Delivery Person", description = "")
public class DeliveryPersonController {

    private final DeliveryPersonService deliveryPersonService;
    @PostMapping
    public ResponseEntity<String> saveDeliveryPerson(@RequestBody DeliveryPersonRequestDto deliveryPersonRequestDto){
        deliveryPersonService.createDeliveryPerson(deliveryPersonRequestDto);
        return new ResponseEntity<>("Delivery person saved successfully.", HttpStatus.CREATED);
    }


    @PostMapping("/assign-available-delivery-persons")
    public ResponseEntity<Void> assignAvailableDeliveryPersonsToPendingDeliveries() {
        deliveryPersonService.assignAvailableDeliveryPersonsToPendingDeliveries();
        return ResponseEntity.ok().build();
    }

}
