package br.com.fiap.order_management.api.controller;

import br.com.fiap.order_management.domain.usecase.ProcessPaymentUseCase;
import br.com.fiap.order_management.infra.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProcessPaymentUseCase processPaymentUseCase;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        PaymentController paymentController = new PaymentController(processPaymentUseCase);

        mockMvc = MockMvcBuilders.standaloneSetup(paymentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    public void shouldUpdateDelivered() throws Exception {
        UUID paymentId = UUID.randomUUID();

        doNothing().when(processPaymentUseCase).execute(any(UUID.class));

        mockMvc.perform(post("/api/payment/callback/{requestPaymentId}", paymentId))
                .andExpect(status().isOk());

        verify(processPaymentUseCase, times(1)).execute(any(UUID.class));
    }

}