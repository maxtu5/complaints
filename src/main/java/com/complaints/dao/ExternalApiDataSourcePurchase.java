package com.complaints.dao;

import com.complaints.dto.PurchaseDto;
import com.complaints.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExternalApiDataSourcePurchase extends ExternalApiCaller<PurchaseDto> {

    private final String MOCK_PURCHASE_API_URL = "http://localhost:8081/purchases/%s";
    private final RestTemplate restTemplate;

    @PostConstruct
    private void initUrl() {
        super.setUrlExternalApi(MOCK_PURCHASE_API_URL);
    }

    public Optional<PurchaseDto> findById(UUID id) {
        return super.findById(id, PurchaseDto.class, restTemplate);
    }
}
