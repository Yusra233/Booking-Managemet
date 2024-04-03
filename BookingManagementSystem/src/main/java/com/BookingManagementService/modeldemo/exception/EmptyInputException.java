package com.BookingManagementService.modeldemo.exception;

public class EmptyInputException extends RuntimeException{
    public EmptyInputException(String message) {
        super(message);
    }
    public EmptyInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
