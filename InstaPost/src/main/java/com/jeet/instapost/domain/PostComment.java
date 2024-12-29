package com.jeet.instapost.domain;

import com.jeet.instapost.dto.PostCommentRecord;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private String title;

    private String review;

    public PostCommentRecord toPostCommentRecord() {
        return new PostCommentRecord(id, title, review);
    }
}
