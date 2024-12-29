package com.jeet.instapost.dto;

import com.jeet.instapost.domain.Post;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

public record PostRecord(Long id, @NonNull String title,@NonNull String content, List<PostCommentRecord> comments) {

    public Post toPost() {
        Post post = Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .comments(comments.stream().map(PostCommentRecord::toPostComment).collect(Collectors.toList()))
                .build();
        System.out.println(post.toString());
        return post;
    }
}
