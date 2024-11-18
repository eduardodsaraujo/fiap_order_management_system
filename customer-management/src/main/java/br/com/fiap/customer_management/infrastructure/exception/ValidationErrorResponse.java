package br.com.fiap.customer_management.infrastructure.exception;

import lombok.Data;

import java.util.Map;

@Data
public class ValidationErrorResponse extends MessageError {
    private Map<String, String> fieldErrors;
}
