package com.jeet;

import reactor.core.publisher.Flux;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Flux<String> just = Flux.just("Hello", "World");

        just.subscribe(System.out::println);
    }
}
