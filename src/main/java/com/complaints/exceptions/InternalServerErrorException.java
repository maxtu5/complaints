package com.complaints.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends ComplaintServiceException {
    public InternalServerErrorException(String message) {
        super(message);
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
