package com.jeet.beerclient.domain;
import lombok.*;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Beer {
    private UUID id;
    private String beerName;
    private String upc;
    private BeerType beerStyle;
    private double price;
    private int quantityOnHand;
}
