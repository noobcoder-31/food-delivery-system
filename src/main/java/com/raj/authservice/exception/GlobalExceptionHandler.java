package com.raj.authservice.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generalExceptionHandler(Exception e) {

        ErrorResponse response = new ErrorResponse("failure",e.getMessage(),"Something Went Wrong");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validationExceptionHandler(MethodArgumentNotValidException e){
        ErrorResponse response = new ErrorResponse("failure",e.getMessage(),"Validation Error!");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> emailExceptionHandler(EmailAlreadyExistException e){
        ErrorResponse response = new ErrorResponse("failure",e.getMessage(),"User with this email already exists");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> manualLoginExceptionHandler(InvalidCredentialsException e){
        ErrorResponse response = new ErrorResponse("failure",e.getMessage(),"Invalid Credentials.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> loginExceptionHandler(BadCredentialsException e){
        ErrorResponse response = new ErrorResponse("failure",e.getMessage(),"Invalid email or password.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}

