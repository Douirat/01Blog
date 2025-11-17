package com.blog.backend.service;

import java.util.List;
import java.util.Optional;
import com.blog.backend.model.Post;
import org.springframework.stereotype.Service;
import com.blog.backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.blog.backend.dto.post.PostInputDTO;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import com.blog.backend.repository.UserRepository;
import com.blog.backend.model.User;

@Service // ← This tells Spring: "I'm the implementation!"
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    private static final String UPLOAD_DIR = "uploads/";
    private static final String BASE_URL = "http://localhost:8080/";

    // @Override
    // public List<Post> getAllPosts() {
    // return postRepository.findAll();
    // }

    // @Override
    // public Optional<Post> getPostById(Long id) {
    // return postRepository.findById(id);
    // }

    @Override
    public Post createPost(String userId, PostInputDTO post) {
        String mediaUrl = null;
        // Logic to handle media upload and set mediaUrl accordingly would go here:
        if (post.getMediaType() != null && !post.getMediaType().isEmpty()) {
            mediaUrl = saveMedia(post.getMedia());
        }

        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post newPost = new Post();
        newPost.setTitle(post.getTitle());
        newPost.setContent(post.getContent());
        newPost.setMediaType(post.getMediaType());
        newPost.setMediaUrl(mediaUrl);
        newPost.setCreatedAt(LocalDateTime.now());
        newPost.setUser(user);
        return postRepository.save(newPost);
    }

    private String saveMedia(MultipartFile media) {
        try {
            String filename = UUID.randomUUID() + "-" + media.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + filename);
            Files.copy(media.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return "http://localhost:8080/" + UPLOAD_DIR + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store media file", e);
        }
    }

    // @Override
    // public Post updatePost(Long id, Post post) {
    // return postRepository.findById(id)
    // .map(existingPost -> {
    // existingPost.setTitle(post.getTitle());
    // existingPost.setContent(post.getContent());
    // return postRepository.save(existingPost);
    // })
    // .orElseThrow(() -> new RuntimeException("Post not found"));
    // }

    // @Override
    // public void deletePost(Long id) {
    // postRepository.deleteById(id);
    // }
}