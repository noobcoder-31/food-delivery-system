package com.raj.authservice.exception;

public class ErrorResponse {
    private String status;
    private String message;
    private String errors;

    public String getStatus() {
        return status;
    }

    public ErrorResponse(String status, String message, String errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
