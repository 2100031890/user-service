package com.customerproduct.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException() {
        super();
    }
}
