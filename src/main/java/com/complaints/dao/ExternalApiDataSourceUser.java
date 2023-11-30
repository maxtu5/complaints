package com.complaints.dao;

import com.complaints.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
public class ExternalApiDataSourceUser implements ExternalApiDataSource<UserDto> {

    private final String MOCK_USER_API_URL = "http://localhost:8081/users/%s";

    @Override
    public Optional<UserDto> findById(UUID id) {
        String userUrlString = String.format(MOCK_USER_API_URL, id);
        RestTemplate restTemplate = new RestTemplate();
        try {
            UserDto userDto = restTemplate.getForObject(userUrlString, UserDto.class);
            return Optional.ofNullable(userDto);
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving user data");
        }
    }
}
