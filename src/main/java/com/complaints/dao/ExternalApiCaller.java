package com.complaints.dao;

import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

public class ExternalApiCaller<T> {

    @Setter
    private String urlExternalApi;

    public Optional<T> findById(UUID id, Class<T> entityClass, RestTemplate restTemplate, String errorMessage) {
        String userUrlString = String.format(urlExternalApi, id);
        try {
            T t = (T) restTemplate.getForObject(userUrlString, entityClass);
            return Optional.ofNullable(t);
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        }
    }

}
