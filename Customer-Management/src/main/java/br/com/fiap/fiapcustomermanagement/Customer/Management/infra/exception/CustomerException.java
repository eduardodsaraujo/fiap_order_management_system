package br.com.fiap.fiapcustomermanagement.Customer.Management.infra.exception;

public class CustomerException extends RuntimeException {
    public CustomerException(String message){
        super(message);
    }
}
