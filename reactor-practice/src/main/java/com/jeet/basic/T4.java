package com.jeet.basic;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class T4 {
    public static void main(String[] args) {
//        handleError();
        handleError2();
    }

    private static void handleError2() {

        Flux.just(1, 2, 3, null, 5)
                .log("OOO") // Error occur here only hats why went to error without onErrorContinue
                .map(x -> x * x)
                .onErrorContinue((item, throwable) -> System.out.println("Item: Not Found"))
                .switchIfEmpty(Flux.just(9999)) // Provide a default if the stream becomes empty after onErrorContinue
                .subscribe(System.out::println,
                        System.err::println,
                        () -> System.out.println("Done!"));

        Flux.just("1", "2", "abc", "4", "5")
                .map(Integer::parseInt) // This can throw a NumberFormatException
                .onErrorContinue((item, throwable) -> System.out.println("Error parsing: " + item + ", Error: "))
                .subscribe(
                        System.out::println,
                        (e) -> System.out.println("Error: ***"),
                        () -> System.out.println("Done!")
                );

    }

    private static void handleError() {


        Mono.error(new RuntimeException("error"))
                .onErrorComplete()
                .subscribe(System.out::println,
                        System.err::println,
                        () -> System.out.println("Done!"));

        Mono.error(new RuntimeException("error"))
                .onErrorReturn(3)
                .subscribe(System.out::println,
                        System.err::println,
                        () -> System.out.println("Done!"));

        Mono.error(new RuntimeException("error"))
                .onErrorReturn(RuntimeException.class, "Apple")
                .onErrorReturn(3)
                .subscribe(System.out::println,
                        System.err::println,
                        () -> System.out.println("Done!"));

        Mono.error(new RuntimeException("error"))
                .onErrorResume(t -> Mono.just("Book"))
                .subscribe(System.out::println,
                        System.err::println,
                        () -> System.out.println("Done!"));


        Flux.just(1, 2, 3, null, 5)
                .map(x -> x * x)
                .onErrorResume(t -> Flux.just(99))
                .subscribe(System.out::println,
                        System.err::println,
                        () -> System.out.println("Done!"));

        Flux.just(1, 2, 3, null, 5)
                .map(x -> x * x)
                .onErrorContinue((item, throwable) -> System.out.println("Item: Not Found"))
                .onErrorReturn(9999)
                .subscribe(System.out::println,
                        System.err::println,
                        () -> System.out.println("Done!"));


    }
}
