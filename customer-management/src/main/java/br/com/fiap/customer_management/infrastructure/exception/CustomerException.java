package br.com.fiap.customer_management.infrastructure.exception;

public class CustomerException extends RuntimeException {
    public CustomerException(String message){
        super(message);
    }
}
