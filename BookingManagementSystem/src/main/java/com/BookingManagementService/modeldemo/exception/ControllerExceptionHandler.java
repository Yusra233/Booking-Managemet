package com.BookingManagementService.modeldemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(value = {EmptyInputException.class})
    public ResponseEntity<ExceptionMessage> handleEmptyInputException(EmptyInputException e){
        ExceptionMessage exceptionMessage=new ExceptionMessage(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(exceptionMessage,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ExceptionMessage> handleNotFoundException(NotFoundException e){
        ExceptionMessage exceptionMessage=new ExceptionMessage(
                e.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(exceptionMessage,HttpStatus.NOT_FOUND);
    }

}
