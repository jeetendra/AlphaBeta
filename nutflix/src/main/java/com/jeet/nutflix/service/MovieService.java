package com.jeet.nutflix.service;

import com.jeet.nutflix.domain.Movie;
import com.jeet.nutflix.event.MovieEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieService {
    Flux<Movie> findAll();
    Mono<Movie> findById(String id);
    Flux<MovieEvent> straamMovieEvents(String id);
}
