package com.complaints.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ComplaintServiceException {
    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getResponseMessage() {
        return "Bad data in request";
    }
}
