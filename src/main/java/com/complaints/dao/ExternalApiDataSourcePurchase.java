package com.complaints.dao;

import com.complaints.dto.PurchaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExternalApiDataSourcePurchase extends ExternalApiCaller<PurchaseDto> {

    private final String MOCK_PURCHASE_API_URL = "http://localhost:8081/purchases/%s";
    private final String notFoundMessage = "Purchase Id not found";

    @PostConstruct
    private void initUrl() {
        super.setUrlExternalApi(MOCK_PURCHASE_API_URL);
    }

    public Mono<Optional<PurchaseDto>> findById(UUID id) {
        return super.findById(id, PurchaseDto.class, notFoundMessage);
    }
}
