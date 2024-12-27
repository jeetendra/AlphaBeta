package com.jeet.nutflix.controller;

import com.jeet.nutflix.domain.Movie;
import com.jeet.nutflix.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

@WebFluxTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    MovieService movieService;

    Movie movie;

    @BeforeEach
    void setUp() {
        movie = Movie.builder().id("1").title("Anaconda").build();
    }

    @Test
    void findAll() {
        Mockito.when(movieService.findAll()).thenReturn(Flux.just(movie));

        webTestClient.get().uri("/movies/all")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Movie.class)
                .hasSize(1);
    }
}