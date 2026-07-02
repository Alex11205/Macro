package com.alex.macro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoSuchUserExistsException extends RuntimeException{


    public NoSuchUserExistsException(String user) {
        super(String.format("User '%s' cannot be found.", user));
    }
}
