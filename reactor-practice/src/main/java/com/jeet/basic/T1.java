package com.jeet.basic;

import org.w3c.dom.ls.LSOutput;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class T1 {
    public static void main(String[] args) {
//        test1();
//        test2();
//        test3();
//        test4();
//        test5();
//        test6();
//        test7();

    }

    private static Mono<String> gerDelayedUpperCase(String string) {
        return Mono.just(string.toUpperCase()).delayElement(Duration.ofSeconds(1));
    }

    private static void test7() {
        List<String> list = List.of("a", "b", "c", "d", "e", "f", "g", "h");
        Flux.fromIterable(list)
                .flatMap(T1::gerDelayedUpperCase)
                .subscribe(System.out::println);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private static void test6() {
        Flux<String> fruits = Flux.just("apple", "banana", "orange");
        Flux<String> colors = Flux.just("red", "yellow", "orange");

        Flux.zip(fruits, colors) // Combines corresponding elements from both Fluxes
                .map(tuple -> tuple.getT1() + " is " + tuple.getT2()) // Formats output
                .subscribe(System.out::println);
    }

    private static void test5() {
        Flux.just(1, 2, 3, 0, 4)
                .map(i -> 10 / i) // This will throw an ArithmeticException when i is 0
                .onErrorReturn(0)    // Returns 0 if an error occurs
                .subscribe(System.out::println);

    }

    private static void test4() {
        List<String> a = List.of("a", "b", "c", "d", "e");
        Flux.fromIterable(a)
                .map(String::toUpperCase)
                .subscribe(
                        (data) -> System.out.println(data)
                );
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void test3() {
        Flux.error(new RuntimeException("Errrrrr")).subscribe(System.out::println, (throwable) -> System.out.println("Error"), () -> System.out.println("Completed"));

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void test2() {
        Flux.empty().subscribe(System.out::println, null, () -> System.out.println("Completed"));
    }

    private static void test1() {
        Flux.interval(Duration.ofSeconds(1)).subscribe(System.out::println);

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
