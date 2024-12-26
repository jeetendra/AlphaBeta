package com.jeet.nutflix.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    private String id;
    @NonNull
    private String title;
}
