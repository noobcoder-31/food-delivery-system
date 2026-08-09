package com.raj.authservice.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generalExceptionHandler(Exception e) {

        ErrorResponse response = new ErrorResponse(500,e.getMessage(),"Something Went Wrong");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validationExceptionHandler(Exception e){
        ErrorResponse response = new ErrorResponse(400,e.getMessage(),"Validation Error!");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> emailExceptionHandler(Exception e){
        ErrorResponse response = new ErrorResponse(409,e.getMessage(),"User with this email already exists");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}

