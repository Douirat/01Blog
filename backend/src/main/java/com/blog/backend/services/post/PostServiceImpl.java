package com.blog.backend.services.post;

import com.blog.backend.models.post.Post;
import com.blog.backend.repositories.post.PostRepository;
import com.blog.backend.dtos.post.PostInputDTO;
import com.blog.backend.repositories.user.UserRepository;
import com.blog.backend.models.user.User;
import com.blog.backend.services.file.FileStorageService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.blog.backend.constants.FileTypeConstants;
import com.blog.backend.dtos.post.PostDetailDTO;
import com.blog.backend.dtos.post.UserSummaryDTO;
import com.blog.backend.dtos.user.UserDTO;

// Pagination imports:
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import com.blog.backend.models.vote.Vote;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Service // ← This tells Spring: "I'm the implementation!"
public class PostServiceImpl implements PostService {

        // inject the constants:
        @Autowired
        private PostRepository postRepository;
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private FileStorageService fileStorageService;

        // get posts for a specific user:
        @Override
        public Page<PostDetailDTO> getUserPosts(int page, long userId) {
                int size = 10;

                Pageable pageable = PageRequest.of(
                                page,
                                size,
                                Sort.by(Sort.Direction.DESC, "createdAt"));

                Page<Post> posts = postRepository.findAllByUserId(userId, pageable);

                return posts.map(post -> {

                        UserSummaryDTO userSummary = new UserSummaryDTO(
                                        post.getUser().getId(),
                                        post.getUser().getNickname());

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
                                        post.getCreatedAt());

                });

        }

        @Override
        public Page<PostDetailDTO> getAllPosts(int page) {

                int size = 10;

                Pageable pageable = PageRequest.of(
                                page,
                                size,
                                Sort.by(Sort.Direction.DESC, "createdAt") // newest → oldest
                );

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
                                        post.getCreatedAt());
                });

        }

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

        @Override
        @Transactional
        public Post updatePost(Long userId, Long postId, PostInputDTO postInput) {
                Post existingPost = postRepository.findByIdAndUserId(postId, userId)
                                .orElseThrow(() -> new RuntimeException("Post not found or not owned by you"));

                existingPost.setTitle(postInput.getTitle());
                existingPost.setContent(postInput.getContent());

                if (postInput.getMediaType() != null) {
                        existingPost.setMediaType(postInput.getMediaType());
                }
                if (postInput.getMedia() != null) {
                        String mediaUrl = fileStorageService.saveFile(postInput.getMedia(),
                                        FileTypeConstants.POST_MEDIA_DIR, FileTypeConstants.MEDIA_TYPES);
                        existingPost.setMediaUrl(mediaUrl);
                }

                return postRepository.save(existingPost);
        }

        @Override
        public UserDTO getUserByPostId(long postId) {
                return postRepository.findById(postId)
                                .map(Post::getUser)
                                .map(this::toDTO) // Assume you have a mapper method
                                .orElse(null);
        }

        private UserDTO toDTO(User user) {
                return new UserDTO(
                                user.getId(),
                                user.getEmail(),
                                user.getFirstName(),
                                user.getLastName(),
                                user.getAvatar(),
                                user.getNickname(),
                                user.getDateOfBirth(),
                                user.isAdmin());
        }

        @Override
        public Page<PostDetailDTO> getReportedPosts(int page, long userId) {
                int size = 10;
                Pageable pageable = PageRequest.of(
                                page,
                                size,
                                Sort.by(Sort.Direction.DESC, "createdAt"));
                Page<Post> posts = this.postRepository.findAllReportedPosts(pageable, userId);
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
                                        post.getCreatedAt());
                });
        }
}