package com.jeet.beerclient;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class FluxTests {

    @Test
    void testFluxDefer() {
        Flux<String> just = Flux.just("A", "B", "C", "D", "E")
                .filter(s -> s.length() > 1)
                .switchIfEmpty(Flux.defer(() -> Flux.just("AA", "BA", "C", "D", "E")));


        just.subscribe(System.out::println, e -> System.out.println("Error: "));


    }
}
