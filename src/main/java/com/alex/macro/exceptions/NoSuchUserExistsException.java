package com.alex.macro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoSuchUserExistsException extends RuntimeException{
    private String message;

    public NoSuchUserExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}
