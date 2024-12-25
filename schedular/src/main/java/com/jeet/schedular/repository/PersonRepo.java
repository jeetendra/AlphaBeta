package com.jeet.schedular.repository;

import com.jeet.schedular.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonRepo {

    Mono<Person> save(Person person);

    Mono<Person> update(Person person);

    Mono<Person> delete(Person person);

    Flux<Person> findAll();

    Mono<Person> findById(int id);

}
