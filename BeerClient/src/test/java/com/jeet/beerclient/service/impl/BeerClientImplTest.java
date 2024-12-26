package com.jeet.beerclient.service.impl;

import com.jeet.beerclient.config.WebClientConfig;
import com.jeet.beerclient.domain.Beer;
import com.jeet.beerclient.domain.BeerPagedList;
import com.jeet.beerclient.domain.BeerType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class BeerClientImplTest {

    BeerClientImpl beerClient;

    @BeforeEach
    void setUp() {
        var baseUrl = "http://api.springframework.guru";
        beerClient = new BeerClientImpl(new WebClientConfig().webClient(WebClient.builder(), baseUrl));
    }

    @Test
    void getBeerById() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList beerPagedList = beerPagedListMono.block();
        UUID id = beerPagedList.getContent().get(0).getId();

        Mono<Beer> beerMono = beerClient.getBeerById(id, false);
        Beer beer = beerMono.block();
        assertThat(beer).isNotNull();
    }

    @Test
    void listBeers() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList pageList = beerPagedListMono.block();
        System.out.println(pageList);

        Assertions.assertNotNull(pageList);

        assertThat(pageList.getContent().size()).isGreaterThan(0);

        System.out.println(pageList.getTotalElements());

    }

    @Test
    void createBeer() {
        Beer beer = Beer.builder()
                .beerName("A")
                .beerStyle(BeerType.GOSE)
                .price(10)
                .quantityOnHand(2)
                .upc("upc")
                .build();
        Mono<ResponseEntity<Void>> response = beerClient.createBeer(beer);
        ResponseEntity<Void> block = response.block();
        assertThat(block.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void updateBeer() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList beerPagedList = beerPagedListMono.block();
        Beer beer = beerPagedList.getContent().get(0);

        Beer newBeer = Beer.builder()
                .beerName("A")
                .beerStyle(BeerType.GOSE)
                .price(10)
                .quantityOnHand(2)
                .upc("upc")
                .build();

        Mono<ResponseEntity<Void>> response = beerClient.updateBeer(beer.getId(), newBeer);
        ResponseEntity<Void> block = response.block();

        assertThat(block.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteBeerById() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList beerPagedList = beerPagedListMono.block();
        Beer beer = beerPagedList.getContent().get(0);

        Mono<ResponseEntity<Void>> response = beerClient.deleteBeerById(beer.getId());
        ResponseEntity<Void> block = response.block();

        assertThat(block.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void getBeerByUPC() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList pageList = beerPagedListMono.block();
        System.out.println(pageList);
        String upc = pageList.getContent().get(5).getUpc();
        System.out.println(upc);

        Beer block = beerClient.getBeerByUPC(upc).block();
        assertThat(block).isNotNull();
    }
}