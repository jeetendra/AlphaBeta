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
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
    void getBeerByIdReactive() throws InterruptedException {

        AtomicReference<Beer> beerAtomicReference = new AtomicReference<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        beerClient.listBeers(null, null, null, null, null)
                .map(l -> l.getContent().get(0).getId())
                .flatMap(id -> beerClient.getBeerById(id, false))
                .subscribe(beer -> {
                    // If assert fail here then this test will not complete
//                    assertThat(beer).isNotNull();
//                    assertThat(true).isEqualTo(false);
                    beerAtomicReference.set(beer);
                    countDownLatch.countDown();  // **Should be last**
                });
        countDownLatch.await();
        assertThat(beerAtomicReference.get()).isNotNull();   // Right way to test
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
    void deleteBeerByIdNotFound() {
        Mono<ResponseEntity<Void>> response = beerClient.deleteBeerById(UUID.randomUUID());

        assertThrows(WebClientResponseException.class, () -> {
                    ResponseEntity<Void> block = response.block();
                    assertThat(block.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                }
        );
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