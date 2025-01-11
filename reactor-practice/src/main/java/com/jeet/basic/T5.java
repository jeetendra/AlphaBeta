package com.jeet.basic;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

public class T5 {
    public static void main(String[] args) {
//        demo1();
demo2();
    }

    private static void demo2() {
        var sink = Sinks.one();

        var mono = sink.asMono();

        mono.subscribe(System.out::println,
                System.err::println,
                () -> System.out.println("Done!"));

//        sink.tryEmitEmpty();
        sink.tryEmitError(new RuntimeException("ooo"));
    }

    private static void demo1() {
        var sink = Sinks.one();

        var mono = sink.asMono();

        mono.subscribe(System.out::println,
                System.err::println,
                () -> System.out.println("Done!"));

        sink.tryEmitValue("Hello World");

    }
}
