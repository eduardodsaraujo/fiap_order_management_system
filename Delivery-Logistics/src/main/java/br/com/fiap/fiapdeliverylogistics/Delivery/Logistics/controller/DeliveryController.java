package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.controller;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.DeliveryService;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.ChangeDeliveryStatusRequestDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.DeliveryRequestDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.DeliveryResponseDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.DeliveryTrackRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery")
@AllArgsConstructor
@Tag(name = "Delivery", description = "")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<String> createDelivery(@RequestBody DeliveryRequestDto deliveryRequestDto){
        deliveryService.createDelivery(deliveryRequestDto.getOrderId());
        return new ResponseEntity<>("Delivery created successfully.", HttpStatus.CREATED);
    }

    @PutMapping("/update-track")
    public ResponseEntity<DeliveryResponseDto> updateDeliveryTrack(@RequestBody DeliveryTrackRequestDto deliveryTrackRequestDto) {
        DeliveryResponseDto responseDto = deliveryService.saveDeliveryTrack(deliveryTrackRequestDto.getOrderId(),
                deliveryTrackRequestDto.getLatitude(),
                deliveryTrackRequestDto.getLongitude());
        return ResponseEntity.ok(responseDto);

    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponseDto> getDelivery(@PathVariable("id") Long deliveryId) {
        DeliveryResponseDto deliveryResponseDto = deliveryService.getById(deliveryId);
        return ResponseEntity.ok(deliveryResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryResponseDto> updateDeliveryStatus(@PathVariable("id") Long deliveryId,
                                @RequestBody ChangeDeliveryStatusRequestDto changeDeliveryStatusRequestDto) {
        DeliveryResponseDto deliveryResponseDto = deliveryService.changeDeliveryStatus(deliveryId, changeDeliveryStatusRequestDto);
        return ResponseEntity.ok(deliveryResponseDto);
    }

    /*

    private final CustomerService customerService;
    @GetMapping("/fiapcustomermanagement/api/customers/1")
    public String status(){
        return "ok";
    }



    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieve all customers")
    public List<CustomerDTO> getAllDeliveries() {
        return customerService.findAll();
    }

    @PostMapping
    @Operation(summary = "Create a new delivery", description = "Create a new delivery with the provided details")
    public ResponseEntity<CustomerDTO> createDelivery(@RequestBody @Valid CustomerRequestDTO customerRequestDTO) {
        CustomerDTO savedCustomer = customerService.saveCustomer(customerRequestDTO);
        return ResponseEntity.ok(savedCustomer);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Retrieve customer details by ID")
    public ResponseEntity<CustomerDTO> getDeliveryById(@PathVariable Long id) {
        CustomerDTO customer = customerService.findById(id);
        return ResponseEntity.ok(customer);
    }




        @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer", description = "Delete a customer by ID")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
*/

}