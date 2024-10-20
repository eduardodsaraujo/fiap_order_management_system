package br.com.fiap.fiapcustomermanagement.Customer.Management.controller;

import br.com.fiap.fiapcustomermanagement.Customer.Management.dto.AddressDTO;
import br.com.fiap.fiapcustomermanagement.Customer.Management.dto.AddressRequestDTO;
import br.com.fiap.fiapcustomermanagement.Customer.Management.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/addresses")
@AllArgsConstructor
@Tag(name = "Customer address", description = "Operations related to customer addresses")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    @Operation(summary = "Add new address", description = "Add a new address for the specified customer")
    public ResponseEntity<AddressDTO> addAddress(@PathVariable Long customerId, @RequestBody AddressRequestDTO addressRequestDTO) {
        AddressDTO savedAddress = addressService.saveAddress(customerId, addressRequestDTO);
        return ResponseEntity.ok(savedAddress);
    }
    @GetMapping
    @Operation(summary = "Get all addresses", description = "Retrieve all addresses for the specified customer")
    public ResponseEntity<List<AddressDTO>> getAllAddresses(@PathVariable Long customerId) {
        List<AddressDTO> addresses = addressService.findAllAddresses(customerId);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{addressId}")
    @Operation(summary = "Get address by ID", description = "Retrieve a specific address by its ID for the specified customer")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long customerId, @PathVariable Long addressId) {
        AddressDTO address = addressService.findAddressById(customerId, addressId);
        return ResponseEntity.ok(address);
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "Update address", description = "Update an existing address for the specified customer")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long customerId, @PathVariable Long addressId, @RequestBody AddressRequestDTO addressRequestDTO) {
        AddressDTO updatedAddress = addressService.updateAddress(customerId, addressId, addressRequestDTO);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "Delete address", description = "Delete an address for the specified customer")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long customerId, @PathVariable Long addressId) {
        addressService.deleteAddress(customerId, addressId);
        return ResponseEntity.noContent().build();
    }
}