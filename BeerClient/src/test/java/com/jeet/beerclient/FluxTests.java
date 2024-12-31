package com.jeet.beerclient;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

public class FluxTests {

    @Test
    void test() {
        Flux<String> just = Flux.just("A", "B", "C", "D", "E")
                .filter(s -> s.length() > 1)
                .switchIfEmpty(Flux.defer(() -> Flux.just("AA", "BA", "C", "D", "E"))); // if we don't use defer then these will be emited

        just.subscribe(System.out::println, e -> System.out.println("Error: "));
    }


    @Test
    void test2() {
        Stream.of("A", "B", "C", "D", "E")
                .peek(s -> System.out.println("peek: " + s))
                .toList();

        Mono.just("A").subscribe(System.out::println, e -> System.out.println("Error: "));
        Mono.just(1).subscribe(System.out::println, e -> System.out.println("Error: "));

    }

    @Test
    void test3() {
        var x = 5;
        var defered = Mono.defer(() -> Mono.just(x));
//        x = 6;   // We cant change value.
        defered.subscribe(System.out::println, e -> System.out.println("Error: "));
    }

    @Test
    void test4() {
        // Will not return anything
        Mono.error(new RuntimeException()).subscribe(
                System.out::println,
                e -> System.out.println("Error: "),
                () ->System.out.println("Completed"));
    }

    @Test
    void test5() {
        // Will not return anything
        Mono.empty().subscribe(
                System.out::println,
                e -> System.out.println("Error: "),
                () ->System.out.println("Completed"));
    }

    @Test
    void test6() {
        Mono.fromSupplier(() -> "Hello").subscribe(System.out::println, e -> System.out.println("Error: "));
    }
}
