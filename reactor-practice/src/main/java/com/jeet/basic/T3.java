package com.jeet.basic;

import reactor.core.publisher.Flux;

public class T3 {
    public static void main(String[] args) {
//        test1();
//        test2();
//        test3();
        test4();
    }

    private static void test4() {
        Flux.range(1, 10)
                .handle((item, sink) -> {
                    if(item %2 == 0) {
                        sink.next(item);
                    } else {
                        sink.next(item*2);
                    }
                })
                .subscribe(System.out::println);
    }

    private static void test3() {
        Flux.range(1, 10)
                .log("range-takeUntil")
                .takeUntil(x -> x > 5)  // work for falsy
                .log("take-subscriber")
                .subscribe(System.out::println);
    }

    private static void test2() {
        Flux.range(1, 10)
                .log("range-takeWhile")
                .takeWhile(x -> x < 5) // work for truthy
                .log("take-subscriber")
                .subscribe(System.out::println);
    }

    private static void test1() {
        Flux.range(1, 10)
                .log("range-take")
                .take(5)
                .log("take-subscriber")
                .subscribe(System.out::println);
    }
}
