package com.jeet.nutflix.bootstrap;

import com.jeet.nutflix.domain.Movie;
import com.jeet.nutflix.repository.MovieRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitMovie implements CommandLineRunner {

    private final MovieRepo movieRepo;
    private final WebClient webClient;

//    List<Movie> movies = List.of(Movie.builder().id(1L).title("Avatar").description("some desc").build());

    @Override
    public void run(String... args) throws Exception {
        movieRepo.deleteAll()
                .thenMany(
                        Flux.just("Silence of the Lambdas", "AEon Flux", "Enter the Mono<Void>", "The Fluxxinator",
                                        "Back to the Future", "Meet the Fluxes", "Lord of the Fluxes")
                                .map(Movie::new)
                                .flatMap(movieRepo::save)
                ).subscribe(null, null, () -> {
                    movieRepo.findAll()
                            .subscribe(System.out::println);
                });


//        getMovies().subscribe(e -> {
//            System.out.println(e);
//        });


    }

//    private Flux<Movie> getMovies() {
//        return webClient.get()
//                .uri("https://gist.githubusercontent.com/saniyusuf/406b843afdfb9c6a86e25753fe2761f4/raw/523c324c7fcc36efab8224f9ebb7556c09b69a14/Film.JSON")
//                .retrieve()
////                .onStatus(HttpStatus::is4xxClientError, response -> {
////                    return Flux.error(new RuntimeException("Client error"));
////                })
////                .onStatus(HttpStatus::is5xxServerError, response -> {
////                    return Flux.error(new RuntimeException("Server error"));
////                })
//
//                .bodyToFlux(Movie.class)
//                .doOnError(e -> {
//                    System.out.println("Error occurred: " + e.getMessage());
//                });
//    }

//}
}