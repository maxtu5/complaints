package com.complaints.dao;

import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.net.ConnectException;
import java.util.UUID;

public class ExternalApiCaller<T> {

    @Setter
    private String urlExternalApi;

    public Mono<T> findById(UUID id, Class<T> entityClass, String errorMessage) {
        String userUrlString = String.format(urlExternalApi, id);
        Scheduler scheduler = Schedulers.newBoundedElastic(5, 10, "MyThreadGroup");
        return WebClient.create(userUrlString).get()
                .retrieve()
                .bodyToMono(entityClass)
                .publishOn(scheduler)
                .onErrorResume(throwable -> throwable instanceof WebClientResponseException || throwable instanceof ConnectException,
                        t -> Mono.error(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage)));
    }

}
