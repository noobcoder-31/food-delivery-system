package com.raj.authservice.exception;

public class EmailAlreadyExistException extends RuntimeException{
    public EmailAlreadyExistException(String ex){
        super(ex);
    }
}
