package com.jeet.instapost.dto;

import com.jeet.instapost.domain.PostComment;
import jakarta.persistence.Entity;


public record PostCommentRecord(Long id, String title, String review) {

    public PostComment toPostComment() {
        return PostComment.builder()
                .id(id)
                .title(title)
                .review(review)
                .build();
    }
}
