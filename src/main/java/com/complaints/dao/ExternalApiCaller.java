package com.complaints.dao;

import com.complaints.exceptions.InternalServerErrorException;
import com.complaints.exceptions.ItemNotFoundException;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;
import java.util.UUID;

public class ExternalApiCaller<T> {

    public static final String BASE_EXTERNAL_API_URL = "http://localhost:8081";
    @Setter
    private String urlExternalApi;

    WebClient webClient = WebClient.create(BASE_EXTERNAL_API_URL);

    public Mono<Optional<T>> findById(UUID id, Class<T> entityClass, String notFoundMessage) {
        String userUrlString = String.format(urlExternalApi, id);
        return webClient.get()
                .uri(userUrlString)
                .retrieve()
                .bodyToMono(entityClass)
                .publishOn(Schedulers.boundedElastic())
                .onErrorResume(e -> Mono.error(new InternalServerErrorException("External API error")))
                .map(Optional::ofNullable)
                .switchIfEmpty(Mono.just(Optional.empty()));
    }

}
