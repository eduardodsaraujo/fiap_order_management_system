package br.com.fiap.product_management.infra.exception;

import lombok.Data;

import java.util.Map;

@Data
public class ValidationErrorResponse extends MessageError {

    private Map<String, String> fieldErrors;

}
