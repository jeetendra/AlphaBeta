package com.jeet.instapost.repository;

import com.jeet.instapost.domain.PostComment;
import com.jeet.instapost.dto.PostCommentRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends CrudRepository<PostComment, Long> {
    @Query("""
                select new com.jeet.instapost.dto.PostCommentRecord(p.id, p.title, p.review)
                    from PostComment p
                    where p.title like concat('%', :postTitle, '%')

            """)
    List<PostCommentRecord> getCommentsByTitle(@Param("postTitle") String postTitle);
}
