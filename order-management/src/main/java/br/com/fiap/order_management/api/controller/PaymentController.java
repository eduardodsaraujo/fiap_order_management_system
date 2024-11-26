package br.com.fiap.order_management.api.controller;

import br.com.fiap.order_management.domain.usecase.ProcessPaymentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/payment/callback")
@RequiredArgsConstructor
public class PaymentController {

    private final ProcessPaymentUseCase processPaymentUseCase;

    @PostMapping("/{requestPaymentId}")
    public void notifyPayment(@PathVariable UUID requestPaymentId){
        processPaymentUseCase.execute(requestPaymentId);
    }


}
