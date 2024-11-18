package br.com.fiap.order_management.infra.exception;

public class OrderException extends RuntimeException {

    public OrderException(String message){
        super(message);
    }

}
