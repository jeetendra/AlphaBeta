package com.jeet.instapost.dto;

import com.jeet.instapost.domain.Post;
import com.jeet.instapost.domain.PostComment;
import lombok.NonNull;
import org.hibernate.annotations.Comments;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record PostRecord(Long id, @NonNull String title, @NonNull String content, List<PostCommentRecord> comments) {

    public Post toPost() {

        Post post = Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .build();

        if(Objects.nonNull(comments)) {
            List<PostComment> commentsList = comments.stream()
                    .map(PostCommentRecord::toPostComment)
                    .map(c -> {
                        c.setPost(post);
                        return c;
                    })
                    .toList();
            post.setComments(commentsList);
        } else {
            post.setComments(List.of());
        }
        System.out.println(post.toString());
        return post;
    }
}
