package com.complaints.dao;

import com.complaints.dto.PurchaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExternalApiDataSourcePurchase extends ExternalApiCaller<PurchaseDto> {

    private final String MOCK_PURCHASE_API_URL = "http://localhost:8081/purchases/%s";
    private final String errorMessage = "Error retrieving purchase data";

    private final RestTemplate restTemplate;

    @PostConstruct
    private void initUrl() {
        super.setUrlExternalApi(MOCK_PURCHASE_API_URL);
    }

    public Optional<PurchaseDto> findById(UUID id) {
        return super.findById(id, PurchaseDto.class, restTemplate, errorMessage);
    }
}
