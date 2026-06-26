package com.alex.macro.exceptions;

public class FoodAlreadyExistsException extends RuntimeException {
    public FoodAlreadyExistsException(String food) {
        super(String.format("Food '%s' already exists.", food));
    }
}
