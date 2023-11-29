package com.example.demo.dao;

import com.example.demo.dto.PurchaseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
public class ExternalApiDataSourcePurchase implements ExternalApiDataSource<PurchaseDto> {

    private final String MOCK_PURCHASE_API_URL = "http://localhost:8081/purchases/%s";

    @Override
    public Optional<PurchaseDto> findById(UUID id) {
        String purchaseUrlString = String.format(MOCK_PURCHASE_API_URL, id);
        RestTemplate restTemplate = new RestTemplate();
        try {
            PurchaseDto purchaseDto = restTemplate.getForObject(purchaseUrlString, PurchaseDto.class);
            return Optional.ofNullable(purchaseDto);
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving purchase data");
        }
    }
}
