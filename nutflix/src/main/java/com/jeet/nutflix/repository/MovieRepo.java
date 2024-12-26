package com.jeet.nutflix.repository;

import com.jeet.nutflix.domain.Movie;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepo extends ReactiveMongoRepository<Movie, String> {
}
