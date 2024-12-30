package com.jeet.instapost.service;

import com.jeet.instapost.dto.PostCommentRecord;
import com.jeet.instapost.dto.PostRecord;
import com.jeet.instapost.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@ActiveProfiles("test")
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();

        PostRecord postRecord1 = new PostRecord(null, "Title1", "Content", List.of(
                new PostCommentRecord(null, "CommentTitle", "Review 1"),
                new PostCommentRecord(null, "CommentTitle2", "Review 2")
        ));

        PostRecord postRecord2 = new PostRecord(null, "Title2", "Content2", List.of(
                new PostCommentRecord(null, "CommentTitle", "Review 3"),
                new PostCommentRecord(null, "CommentTitle2", "Review 4")
        ));

        postService.savePost(postRecord1);
        postService.savePost(postRecord2);
    }

    @Test
//    @Disabled
    void findAll() {
        // Act
        List<PostRecord> posts = postService.findAll();

        assertNotNull(posts);
        assertEquals(2, posts.size());
        assertTrue(posts.stream().anyMatch(post -> post.title().equals("Title1")));
        assertTrue(posts.stream().anyMatch(post -> post.title().equals("Title2")));
        assertTrue(posts.getFirst().comments().size() == 2, "have 2 comments");
    }

    //    @Disabled
    @Test
    void findById() {
        List<PostRecord> posts = postService.findAll();
        Long id = posts.get(0).id();
        PostRecord post = postService.findById(id);

        assertNotNull(post);
        assertTrue(post.title().equals("Title1"));
        assertTrue(post.content().equals("Content"));
    }

    @Test
    void savePost() {
        PostRecord postRecord1 = new PostRecord(null, "Title1", "Content", List.of(
                new PostCommentRecord(null, "CommentTitle", "Review 1"),
                new PostCommentRecord(null, "CommentTitle2", "Review 2")
        ));
        PostRecord postRecord = postService.savePost(postRecord1);

        assertNotNull(postRecord.id());
        assertTrue(postRecord.id() > 0, "Have positive post_id");
    }
}