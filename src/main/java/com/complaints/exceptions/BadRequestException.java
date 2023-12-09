package com.complaints.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public class BadRequestException extends ComplaintServiceException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(List<String> messages) {
        super(messages);
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
