package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.infrastructure.exception;

import lombok.Data;

import java.util.Map;

@Data
public class ValidationErrorResponse extends MessageError {
    private Map<String, String> fieldErrors;
}
