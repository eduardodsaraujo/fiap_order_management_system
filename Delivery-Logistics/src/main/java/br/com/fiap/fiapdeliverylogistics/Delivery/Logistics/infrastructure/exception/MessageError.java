package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.infrastructure.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageError {

    public LocalDateTime timestamp;

    public Integer status;

    public String message;
}
