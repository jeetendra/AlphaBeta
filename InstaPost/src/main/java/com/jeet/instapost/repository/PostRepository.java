package com.jeet.instapost.repository;

import com.jeet.instapost.domain.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends CrudRepository<Post, Long>, PagingAndSortingRepository<Post, Long> {

    @Query("""
            select p from Post p join p.comments
                where p.id = :id
            """)
    Optional<Post> findWithCommentsById(Long id);

    @Query("""
            select p from Post p join p.comments
                        where true 
            """)
    List<Post> findAllWithComments();

}
