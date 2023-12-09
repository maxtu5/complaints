package com.complaints.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public class InternalServerErrorException extends ComplaintServiceException {
    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(List<String> errors) {
        super(errors);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public String getResponseMessage() {
        return "Internal server error";
    }
}
