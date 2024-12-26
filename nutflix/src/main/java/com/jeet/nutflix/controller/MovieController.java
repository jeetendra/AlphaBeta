package com.jeet.nutflix.controller;

import com.jeet.nutflix.domain.Movie;
import com.jeet.nutflix.event.MovieEvent;
import com.jeet.nutflix.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/all")
    public Flux<Movie> findAll() {
        return movieService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Movie> findById(@PathVariable String id) {
        return movieService.findById(id);
    }

    @GetMapping(value = "/{id}/event", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MovieEvent> findByEvent(@PathVariable String id) {
        return movieService.straamMovieEvents(id);
    }


}
