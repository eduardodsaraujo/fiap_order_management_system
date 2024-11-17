package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.api.controller;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.CalculateShippingService;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.DeliveryService;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.impl.CalculateShippingServiceImpl;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.impl.DeliveryServiceImpl;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.dto.delivery.ChangeDeliveryStatusRequestDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.dto.delivery.DeliveryRequestDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.dto.delivery.DeliveryResponseDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.dto.shipping.CalculateShippingRequestDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.dto.shipping.CalculateShippingResponseDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.dto.delivery.DeliveryTrackRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/delivery")
@AllArgsConstructor
@Tag(name = "Delivery", description = "Operations related to delivery management," +
        " including creating, tracking, and updating delivery statuses.")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final CalculateShippingService calculateShippingService;

    @PostMapping
    @Operation(summary = "Create a new delivery", description = "Creates a new delivery record based on the provided delivery details.")
    public ResponseEntity<DeliveryResponseDto> createDelivery(@RequestBody @Valid DeliveryRequestDto deliveryRequestDto){
        DeliveryResponseDto responseDto = deliveryService.createDelivery(deliveryRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/update-track")
    @Operation(summary = "Update delivery tracking information", description = "Updates the delivery tracking information with the latest latitude and longitude for the specified order.")
    public ResponseEntity<DeliveryResponseDto> updateDeliveryTrack(@RequestBody @Valid  DeliveryTrackRequestDto deliveryTrackRequestDto) {
        DeliveryResponseDto responseDto = deliveryService.saveDeliveryTrack(deliveryTrackRequestDto.getOrderId(),
                deliveryTrackRequestDto.getLatitude(),
                deliveryTrackRequestDto.getLongitude());
        return ResponseEntity.ok(responseDto);

    }

    @GetMapping("/{id}")
    @Operation(summary = "Get delivery by ID", description = "Retrieves delivery details for the specified delivery ID.")
    public ResponseEntity<DeliveryResponseDto> getDelivery(@PathVariable("id") UUID deliveryId) {
        DeliveryResponseDto deliveryResponseDto = deliveryService.getById(deliveryId);
        return ResponseEntity.ok(deliveryResponseDto);
    }

    @GetMapping
    @Operation(summary = "Get all deliveries with pagination", description = "Retrieves a paginated list of all deliveries. Default page size is 10.")
    public ResponseEntity<Page<DeliveryResponseDto>> getAllDeliveries(Pageable pageable) {
        return ResponseEntity.ok(deliveryService.findAllDeliveries(pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update delivery status", description = "Changes the status of a delivery based on the provided status change details.")
    public ResponseEntity<DeliveryResponseDto> updateDeliveryStatus(@PathVariable("id") UUID deliveryId,
                                @RequestBody ChangeDeliveryStatusRequestDto changeDeliveryStatusRequestDto) {
        DeliveryResponseDto deliveryResponseDto = deliveryService.changeDeliveryStatus(deliveryId, changeDeliveryStatusRequestDto);
        return ResponseEntity.ok(deliveryResponseDto);
    }

    @PostMapping("/calculate-shipping")
    @Operation(summary = "Calculate shipping cost and ETA", description = "Calculates the shipping cost and estimated time of arrival based on the delivery details.")
    public ResponseEntity<CalculateShippingResponseDto> calculateShipping(@RequestBody @Valid  CalculateShippingRequestDto requestDto){
        CalculateShippingResponseDto responseDto = calculateShippingService.calculateShipping(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}