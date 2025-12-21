package com.blog.backend.services.post;

import com.blog.backend.models.post.Post;
import com.blog.backend.dtos.post.PostDetailDTO;
import com.blog.backend.repositories.post.PostRepository;
import com.blog.backend.dtos.post.PostInputDTO;
import com.blog.backend.repositories.user.UserRepository;
import com.blog.backend.models.user.User;
import com.blog.backend.services.file.FileStorageService;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Page<PostDetailDTO> getAllPosts(int page);

    Page<PostDetailDTO> getUserPosts(int page, Long userId);

    // Optional<Post> getPostById(Long id);
    Post createPost(Long userId, PostInputDTO post);
    Post updatePost(Long userId, Long postId, PostInputDTO dto);;
    
    // void deletePost(Long id);
}
