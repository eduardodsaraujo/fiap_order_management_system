package br.com.fiap.product_management.infra.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageError {

    public LocalDateTime timestamp;

    public Integer status;

    public String message;
}
