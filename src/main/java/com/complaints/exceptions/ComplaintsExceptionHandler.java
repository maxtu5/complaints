package com.complaints.exceptions;

import com.complaints.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ComplaintsExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiError> handleValidationExceptions(final MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.toString(), "Request is invalid", errors);
        apiError.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ComplaintServiceException.class})
    public ResponseEntity<ApiError> handleNotFoundExceptions(final ComplaintServiceException ex, WebRequest request) {
        ApiError apiError = ex.getErrors()==null ?
                new ApiError(ex.getStatus().toString(), ex.getResponseMessage(), ex.getLocalizedMessage()) :
                new ApiError(ex.getStatus().toString(), ex.getResponseMessage(), ex.getErrors());
        apiError.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(apiError, ex.getStatus());
    }
}

