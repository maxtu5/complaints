package com.complaints.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

public abstract class ComplaintServiceException extends RuntimeException {
@Getter
    private List<String> errors;

    public ComplaintServiceException(String message) {
        super(message);
    }

    public ComplaintServiceException(List<String> errors) {
        super();
        this.errors = errors;
    }

    public abstract HttpStatus getStatus();

    public abstract String getResponseMessage();
}
