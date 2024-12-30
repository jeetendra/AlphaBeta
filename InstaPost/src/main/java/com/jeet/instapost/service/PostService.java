package com.jeet.instapost.service;

import com.jeet.instapost.domain.Post;
import com.jeet.instapost.dto.PostRecord;
import com.jeet.instapost.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<PostRecord> findAll() {
        List<Post> posts = (List<Post>) postRepository.findAllWithComments();
        System.out.println(posts);
        return posts.stream().map(el -> el.toPostRecord()).collect(Collectors.toList());
    }

    public PostRecord findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity Not Found"));
        return post.toPostRecord();
    }

    @Transactional
    public PostRecord savePost(PostRecord postToSave) {
        Post post = postRepository.save(postToSave.toPost());
        System.out.println(post);

        PostRecord postRecord = post.toPostRecord();
        System.out.println(postRecord);
        return postRecord;
    }

    @Transactional
    public PostRecord updatePost(Long id, PostRecord post) {
        Optional<Post> withCommentsById = postRepository.findWithCommentsById(id);
        if (withCommentsById.isPresent()) {
            return postRepository.save(post.toPost()).toPostRecord();
        }
        throw new EntityNotFoundException("Entity Not Found");
    }
}
