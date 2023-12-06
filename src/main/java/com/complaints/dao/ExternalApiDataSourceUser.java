package com.complaints.dao;

import com.complaints.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExternalApiDataSourceUser extends ExternalApiCaller<UserDto> {

    private final String MOCK_USER_API_URL = "http://localhost:8081/users/%s";
    private final RestTemplate restTemplate;

    @PostConstruct
    private void initUrl() {
        super.setUrlExternalApi(MOCK_USER_API_URL);
    }

    public Optional<UserDto> findById(UUID id) {
        return super.findById(id, UserDto.class, restTemplate);
    }
}
