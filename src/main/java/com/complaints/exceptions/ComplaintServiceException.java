package com.complaints.exceptions;

import org.springframework.http.HttpStatus;

public abstract class ComplaintServiceException extends RuntimeException {

    public ComplaintServiceException(String message) {
        super(message);
    }

    public abstract HttpStatus getStatus();

    public abstract String getResponseMessage();
}
