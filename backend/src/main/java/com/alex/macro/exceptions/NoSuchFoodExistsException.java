package com.alex.macro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoSuchFoodExistsException extends RuntimeException{

    public NoSuchFoodExistsException(String food) {
        super(String.format("Food '%s' cannot be found.", food));

    }
}
