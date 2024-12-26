package com.jeet.beerclient.service.impl;

import com.jeet.beerclient.domain.Beer;
import com.jeet.beerclient.domain.BeerPagedList;
import com.jeet.beerclient.service.BeerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeerClientImpl implements BeerClient {
    private final WebClient webClient;


    @Override
    public Mono<Beer> getBeerById(UUID id, Boolean showInventoryOnHand) {
        return webClient.get().uri(uriBuilder ->
                uriBuilder.path("/api/v1/beer/{id}")
                        .queryParamIfPresent("showInventoryOnHand", Optional.ofNullable(showInventoryOnHand))
                        .build(id)
        ).retrieve().bodyToMono(Beer.class);
    }

    @Override
    public Mono<BeerPagedList> listBeers(Integer pageNumber, Integer pageSize, String beerName, String beerStyle, Boolean showInventoryOnhand) {
        return webClient.get().uri("/api/v1/beer")
                .retrieve()
                .bodyToMono(BeerPagedList.class);
    }

    @Override
    public Mono<ResponseEntity<Void>> createBeer(Beer beer) {
        return webClient.post().uri("/api/v1/beer")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(beer))
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public Mono<ResponseEntity<Void>> updateBeer(UUID id, Beer beer) {
        return webClient.put().uri(uriBuilder ->
                uriBuilder.path("/api/v1/beer/{id}").build(id)
        ).body(BodyInserters.fromValue(beer)).retrieve().toBodilessEntity();
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteBeerById(UUID id) {
        return webClient.delete().uri("/api/v1/beer/{id}", id).retrieve().toBodilessEntity();
    }

    @Override
    public Mono<Beer> getBeerByUPC(String upc) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                        .path("/api/v1/beerUpc/{upc}")
                        .build(upc))
                .retrieve().bodyToMono(Beer.class);
    }
}
