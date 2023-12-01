package com.complaints.exceptions;

import org.springframework.http.HttpStatus;

public class ItemNotFoundException extends ComplaintServiceException {

    public ItemNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getResponseMessage() {
        return "Item not found";
    }
}
