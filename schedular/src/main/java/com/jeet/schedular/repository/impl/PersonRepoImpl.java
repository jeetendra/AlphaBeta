package com.jeet.schedular.repository.impl;

import com.jeet.schedular.domain.Person;
import com.jeet.schedular.repository.PersonRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class PersonRepoImpl implements PersonRepo {
    List<Person> persons = List.of(Person.builder().id(1).name("Adam").build(), Person.builder().id(2).name("Bob").build(), Person.builder().id(3).name("Jack").build());
    @Override
    public Mono<Person> save(Person person) {
        return null;
    }

    @Override
    public Mono<Person> update(Person person) {
        return null;
    }

    @Override
    public Mono<Person> delete(Person person) {
        return null;
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.fromIterable(persons);
    }

    @Override
    public Mono<Person> findById(int id) {
        return Mono.just(new Person(1, "Adam"));
    }
}
