package com.blog.backend.service;

import java.util.List;
import java.util.Optional;
import com.blog.backend.model.Post;


public interface PostService {
    List<Post> getAllPosts();
    Optional<Post> getPostById(Long id);
    Post createPost(PostInputDTO post);
    Post updatePost(Long id, PostInputDTO post);
    void deletePost(Long id);
} 

