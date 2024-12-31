package com.jeet.basic;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;



public class T2 {
    public static void main(String[] args) {
//        test1();
//        test2();
//        test3();
//        test4();
        test5();

    }

    private static void test5() {
        Flux.from(Mono.just("Hello World"))
                .subscribe(System.out::println);

        Mono.from(Flux.just("Hello World", "Hello World Again"))
                .subscribe(System.out::println);
    }

    private static void test4() {
        // Disposable

        Disposable subscription = Flux.interval(Duration.ofSeconds(1)).subscribe(System.out::println);

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        subscription.dispose();
        System.out.println("Is Disposed: "+ subscription.isDisposed());

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Done.");
    }


    private static void test3() {
        Mono.just("apple").flux().subscribe(System.out::println);

        Mono.empty().flux().subscribe(System.out::println, null, () -> System.out.println("done"));

        Flux<String> stringFlux = Mono.just("banana").concatWith(Mono.just("carrot"));

        stringFlux.subscribe(System.out::println);

    }

    private static void test2() {
        Flux<String> just = Flux.just("apple");
        Mono<String> single = just.single();
        single.subscribe(System.out::println);

        // below code will throw error because single can only emit once  .
        Flux<String> flux = Flux.just("apple", "banana");
        Mono<String> single1 = flux.single();
        single1.subscribe(System.out::println, System.err::println, () -> System.out.println("Done"));

        // Should emit one time
        Flux.empty().single().subscribe(System.out::println, System.err::println, () -> System.out.println("Done"));

        Flux.empty().singleOrEmpty().subscribe(System.out::println, System.err::println, () -> System.out.println("Done Single oR Empty"));

    }


    private static void test1() {
        Flux<String> flx = Flux.just("a", "b", "c");
        Mono<String> mno = flx.next();

        mno.subscribe(System.out::println);

        Flux<String> flx2 = Flux.empty();
        Mono<String> mno2 = flx2.next();
        mno2.subscribe(System.out::println); // EMPTY
    }
}
