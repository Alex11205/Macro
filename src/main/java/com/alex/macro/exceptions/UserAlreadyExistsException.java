package com.alex.macro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserAlreadyExistsException extends RuntimeException{
    private String message;

    public UserAlreadyExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}
