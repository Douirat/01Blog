package com.blog.backend.service;

import java.util.List;
import java.util.Optional;
import com.blog.backend.model.Post;
import org.springframework.stereotype.Service;
import com.blog.backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service // ← This tells Spring: "I'm the implementation!"
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public Post createPost(PostInputDTO post) {
        String mediaUrl = null;
        // Logic to handle media upload and set mediaUrl accordingly would go here:
        if (post.getMediaType() != null && !post.getMediaType().isEmpty()) {
            mediaUrl = saveMedia(post.getMedia());
        }
         Post newPost = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setMediaType(dto.getMediaType());
        newPost.setMediaUrl(mediaUrl);
        newPost.setCreatedAt(LocalDateTime.now());
        newPost.setUser(getLoggedUser());
        return postRepository.save(newPost);
    }

    private String saveMedia(MultipartFile media) {
        try {
            // Logic to store the media file and return its URL
            String uploadDir = "uploads/";
            String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return "http://localhost:8080/" + uploadDir + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store media file", e);
        }
    }

    @Override
    public Post updatePost(Long id, Post post) {
        return postRepository.findById(id)
                .map(existingPost -> {
                    existingPost.setTitle(post.getTitle());
                    existingPost.setContent(post.getContent());
                    return postRepository.save(existingPost);
                })
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}