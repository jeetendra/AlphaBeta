package com.jeet.instapost.controller;

import com.jeet.instapost.domain.Post;
import com.jeet.instapost.dto.PostRecord;
import com.jeet.instapost.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<PostRecord> getPosts() {
        return postService.findAll();
    }

    @PostMapping()
    public PostRecord addPost(@RequestBody PostRecord post) {
        return postService.savePost(post);
    }

}
