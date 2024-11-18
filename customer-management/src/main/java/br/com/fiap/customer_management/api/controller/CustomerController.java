package br.com.fiap.customer_management.api.controller;

import br.com.fiap.customer_management.application.dto.CustomerDTO;
import br.com.fiap.customer_management.application.dto.CustomerRequestDTO;
import br.com.fiap.customer_management.application.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@AllArgsConstructor
@Tag(name = "Customer", description = "Operations related to customer management")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieve all customers")
    public List<CustomerDTO> getAllCustomers() {
        return customerService.findAll();
    }

    @PostMapping
    @Operation(summary = "Create a new customer", description = "Create a new customer with the provided details")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody @Valid CustomerRequestDTO customerRequestDTO) {
        CustomerDTO savedCustomer = customerService.saveCustomer(customerRequestDTO);
        return ResponseEntity.ok(savedCustomer);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Retrieve customer details by ID")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        CustomerDTO customer = customerService.findById(id);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer", description = "Update an existing customer's details")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long id,
            @RequestBody @Valid CustomerRequestDTO customerRequestDTO) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerRequestDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer", description = "Delete a customer by ID")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

}