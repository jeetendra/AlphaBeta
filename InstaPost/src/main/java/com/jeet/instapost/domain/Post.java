package com.jeet.instapost.domain;

import com.jeet.instapost.dto.PostRecord;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> comments;

    public PostRecord toPostRecord() {
        return new PostRecord(id, title, content,
                Objects.isNull(comments) ? List.of() : comments.stream().map(el -> el.toPostCommentRecord()).toList());
    }
}
