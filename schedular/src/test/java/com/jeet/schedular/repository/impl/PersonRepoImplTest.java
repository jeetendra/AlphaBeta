package com.jeet.schedular.repository.impl;

import com.jeet.schedular.domain.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PersonRepoImplTest {

    private PersonRepoImpl personRepo;

    @BeforeEach
    void setUp() {
        personRepo = new PersonRepoImpl();
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
        Mono<Person> personMono = personRepo.findById(1);
        Person person = personMono.block();
        System.out.println(person);

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();
    }

    @Test
    void getByUdSubscriber() {
        Mono<Person> personMono = personRepo.findById(1);
        personMono.subscribe(person -> {
            System.out.println(person);
        });

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();
    }

    @Test
    void getByIdMap() {
        Mono<Person> personMono = personRepo.findById(1);
        personMono.map(Person::getName)
                .subscribe(System.out::println);

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();
    }

    @Test
    void fluxTest() {
        Flux<Person> personFlux = personRepo.findAll();
        personFlux.subscribe(System.out::println);

        StepVerifier.create(personFlux).expectNextCount(3).verifyComplete();
    }

    @Test
    void fluxTestBlock() {
        Flux<Person> personFlux = personRepo.findAll();
        Person person = personFlux.blockFirst();
        System.out.println(person);

        Assertions.assertNotNull(person);
    }

    @Test
    void fluxToListMono() {
        Flux<Person> personFlux = personRepo.findAll();
        Mono<List<Person>> listMono = personFlux.collectList();
        listMono.subscribe(list -> {
            list.forEach(System.out::println);
        });
        StepVerifier.create(listMono).expectNextCount(1).verifyComplete();
    }

    @Test
    void fluxFilter() {
        Flux<Person> personFlux = personRepo.findAll();

        Mono<Person> mono = personFlux.filter(person -> person.getName().startsWith("B")).next();
        mono.subscribe(System.out::println);

        StepVerifier.create(mono).expectNextCount(1).verifyComplete();
    }

    @Test
    void fluxFilterWithExeception() {
        Flux<Person> personFlux = personRepo.findAll();

        Mono<Person> mono = personFlux.filter(person -> person.getName().startsWith("E")).single(); //It throws error
//        mono.subscribe(System.out::println);


        StepVerifier stepVerifier = StepVerifier.create(mono).expectError();
        stepVerifier.verify();

        mono.doOnError(throwable -> {
            System.out.println("error");
        }).onErrorReturn(Person.builder().build()).subscribe(System.out::println);
    }
}