package com.alex.macro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoSuchFoodExistsException extends RuntimeException{
    private String message;

    public NoSuchFoodExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}
