package com.blog.backend.service;

import java.util.List;
import java.util.Optional;
import com.blog.backend.model.Post;
import com.blog.backend.dto.post.PostInputDTO;



public interface PostService {
    // List<Post> getAllPosts();
    // Optional<Post> getPostById(Long id);
    Post createPost(String userId, PostInputDTO post);
    // Post updatePost(Long id, PostInputDTO post);
    // void deletePost(Long id);
} 

