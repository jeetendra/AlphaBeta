package com.jeet.beerclient.service;

import com.jeet.beerclient.domain.Beer;
import com.jeet.beerclient.domain.BeerPagedList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface BeerClient {



    Mono<Beer> getBeerById(UUID id, Boolean showInventoryOnHand);

    Mono<BeerPagedList> listBeers(Integer pageNumber, Integer pageSize, String beerName,
                                  String beerStyle, Boolean showInventoryOnhand);

    Mono<ResponseEntity<Void>> createBeer(Beer beer);

    Mono<ResponseEntity<Void>> updateBeer(UUID id, Beer beer);

    Mono<ResponseEntity<Void>> deleteBeerById(UUID id);

    Mono<Beer> getBeerByUPC(String upc);
}
