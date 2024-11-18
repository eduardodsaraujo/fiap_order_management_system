package br.com.fiap.delivery_logistics.api.controller;

import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryResponseDto;
import br.com.fiap.delivery_logistics.application.service.DeliveryPersonService;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.ChangeDeliveryPersonStatusRequestDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonRequestDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deliveryperson")
@AllArgsConstructor
@Tag(name = "Delivery Person", description = "Operations related to delivery personnel management, including assignment, status updates, and route calculation.")
public class DeliveryPersonController {

    private final DeliveryPersonService deliveryPersonService;
    @PostMapping
    @Operation(summary = "Register a new delivery person", description = "Creates a new delivery person record with the provided details.")
    public ResponseEntity<DeliveryPersonResponseDto> saveDeliveryPerson(@RequestBody DeliveryPersonRequestDto deliveryPersonRequestDto){
        DeliveryPersonResponseDto responseDto = deliveryPersonService.createDeliveryPerson(deliveryPersonRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update delivery person status", description = "Updates the status of a delivery person, such as availability or assignment status, based on the provided status change details.")
    public ResponseEntity<DeliveryPersonResponseDto> updateDeliveryStatus(@PathVariable("id") Long deliveryId,
                                                                          @RequestBody ChangeDeliveryPersonStatusRequestDto requestDto) {
        DeliveryPersonResponseDto deliveryPersonResponseDto = deliveryPersonService.changeDeliveryPersonStatus(deliveryId, requestDto);
        return ResponseEntity.ok(deliveryPersonResponseDto);
    }

    @GetMapping
    @Operation(summary = "Get all delivery people with pagination", description = "Retrieves a paginated list of all delivery people. Default page size is 10.")
    public ResponseEntity<Page<DeliveryPersonResponseDto>> getAllDeliveries(Pageable pageable) {
        return ResponseEntity.ok(deliveryPersonService.findAllDeliveryPeople(pageable));
    }

    @PostMapping("/assign-available-delivery-people")
    @Operation(summary = "Assign available delivery people to pending deliveries", description = "Automatically assigns available delivery people to pending deliveries that need assignment.")
    public ResponseEntity<Void> assignAvailableDeliveryPeopleToPendingDeliveries() {
        deliveryPersonService.assignAvailableDeliveryPeopleToPendingDeliveries();
        return ResponseEntity.ok().build();
    }
    @GetMapping("/calculate-routes")
    @Operation(summary = "Calculate delivery routes by region", description = "Calculates and returns optimized delivery routes grouped by region.")
    public ResponseEntity<Map<String, List<DeliveryResponseDto>>> calculateRoutes() {
        Map<String, List<DeliveryResponseDto>> routes = deliveryPersonService.calculateRoutesByRegionResponse();
        return ResponseEntity.ok(routes);
    }

}
