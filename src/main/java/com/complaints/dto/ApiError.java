package com.complaints.dto;

import com.complaints.utils.Utc8601InstantSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
public class ApiError {
    @JsonSerialize(using = Utc8601InstantSerializer.class)
    private Instant timestamp;
    private String status;
    private String message;
    private List<String> errors;
    private String path;

    private ApiError() {
        super();
        timestamp = Instant.now();
    }

    public ApiError(String status, String message, List<String> errors) {
        this();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(String status, String message, String error) {
        this();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }
}
