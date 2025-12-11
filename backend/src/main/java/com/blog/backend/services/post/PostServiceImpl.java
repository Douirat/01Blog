package com.blog.backend.services.post;

import com.blog.backend.models.post.Post;
import com.blog.backend.repositories.post.PostRepository;
import com.blog.backend.dtos.post.PostInputDTO;
import com.blog.backend.repositories.user.UserRepository;
import com.blog.backend.models.user.User;
import com.blog.backend.services.file.FileStorageService;
import com.blog.backend.constants.FileTypeConstants;
import com.blog.backend.dtos.post.PostDetailDTO;
import com.blog.backend.dtos.post.UserSummaryDTO;
// Pagination imports:
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import com.blog.backend.models.vote.Vote;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

@Service // ← This tells Spring: "I'm the implementation!"
public class PostServiceImpl implements PostService {

    // inject the constants:
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    // private static final String UPLOAD_DIR = "uploads/";
    // private static final String BASE_URL = "http://localhost:8080/";

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public Page<PostDetailDTO> getAllPosts(int page) {
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(post -> {
            UserSummaryDTO userSummary = new UserSummaryDTO(
                    post.getUser().getId(),
                    post.getUser().getNickname() // matches DTO
            );

            int likes = (int) post.getVotes().stream().filter(Vote::isLiked).count();
            int dislikes = (int) post.getVotes().stream().filter(v -> !v.isLiked()).count();

            int commentsCount = post.getComments().size();

            return new PostDetailDTO(
                    post.getId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getMediaType(),
                    post.getMediaUrl(),
                    userSummary,
                    likes,
                    dislikes,
                    commentsCount,
                    post.getCreatedAt().toString());
        });
    }

    // @Override
    // public Optional<Post> getPostById(Long id) {
    // return postRepository.findById(id);
    // }

    @Override
    public Post createPost(Long userId, PostInputDTO post) {
        String mediaUrl = null;
        // Logic to handle media upload and set mediaUrl accordingly would go here:
        if (post.getMediaType() != null && !post.getMediaType().isEmpty()
                && post.getMedia() != null && !post.getMedia().isEmpty()) {
            mediaUrl = fileStorageService.saveFile(post.getMedia(), FileTypeConstants.POST_MEDIA_DIR,
                    FileTypeConstants.MEDIA_TYPES);
        }

        User user = userRepository.findById(userId)
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