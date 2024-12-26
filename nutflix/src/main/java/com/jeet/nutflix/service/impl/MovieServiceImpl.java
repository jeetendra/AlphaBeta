package com.jeet.nutflix.service.impl;

import com.jeet.nutflix.domain.Movie;
import com.jeet.nutflix.event.MovieEvent;
import com.jeet.nutflix.repository.MovieRepo;
import com.jeet.nutflix.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepo movieRepo;

    @Override
    public Flux<Movie> findAll() {
        return movieRepo.findAll();
    }

    @Override
    public Mono<Movie> findById(String id) {
        return movieRepo.findById(id);
    }

    @Override
    public Flux<MovieEvent> straamMovieEvents(String id) {
        return Flux.<MovieEvent>generate(movieEventSynchronousSink -> {
            movieEventSynchronousSink.next(new MovieEvent(id, new Date()));
        }).delayElements(Duration.ofSeconds(1));
    }
}
