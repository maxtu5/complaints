package com.complaints.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class ReactiveTests {
    @Test
    void simpleFluxExample() {
        Flux<String> fluxColors = Flux.just("red", "green", "blue");
        Flux<Integer> fluxNumbers = Flux.just(1,2,3);
        Flux.zip(fluxColors,fluxNumbers).subscribe(System.out::println);
    }
}
