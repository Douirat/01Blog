package com.blog.backend.services.post;

import com.blog.backend.models.post.Post;
import com.blog.backend.repositories.post.PostRepository;
import com.blog.backend.repositories.report.ReportRepository;
import com.blog.backend.dtos.post.PostInputDTO;
import com.blog.backend.repositories.user.UserRepository;
import com.blog.backend.models.user.User;
import com.blog.backend.services.file.FileStorageService;
import com.blog.backend.types.report.ReportStatus;
import com.blog.backend.constants.FileTypeConstants;
import com.blog.backend.dtos.post.PostDetailDTO;
import com.blog.backend.dtos.post.UserSummaryDTO;
import com.blog.backend.dtos.user.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import com.blog.backend.models.vote.Vote;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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

        @Autowired
        private ReportRepository reportRepository;

        // get posts for a specific user:
        @Override
        public Page<PostDetailDTO> getUserPosts(int page, long userId) {
                int size = 10;

                Pageable pageable = PageRequest.of(
                                page,
                                size,
                                Sort.by(Sort.Direction.DESC, "createdAt"));

                Page<Post> posts = postRepository.findAllByUserIdAndBannedFalse(userId, pageable);

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
                                        post.getCreatedAt(),
                                        post.isBanned());

                });

        }

        @Override
        public Page<PostDetailDTO> getAllPosts(Long userId, int page) {

                int size = 10;

                Pageable pageable = PageRequest.of(
                                page,
                                size,
                                Sort.by(Sort.Direction.DESC, "createdAt") // newest → oldest
                );

                Page<Post> posts = postRepository.findPostsByFollowedUsers(userId, pageable);

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
                                        post.getCreatedAt(),
                                        post.isBanned());
                });

        }

        @Override
        public Post createPost(Long userId, PostInputDTO post) {
                String mediaUrl = null;
                // Logic to handle media upload and set mediaUrl accordingly would go here:
                boolean isKnownMediaType = "image".equals(post.getMediaType()) || "video".equals(post.getMediaType());
                if (isKnownMediaType && post.getMedia() != null && !post.getMedia().isEmpty()) {
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
        public Page<PostDetailDTO> getAllPostsForAdmin(int page) {
                Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
                Page<Post> posts = postRepository.findAllByBannedFalse(pageable);
                return posts.map(post -> {
                        UserSummaryDTO userSummary = new UserSummaryDTO(post.getUser().getId(),
                                        post.getUser().getNickname());
                        int likes = (int) post.getVotes().stream().filter(Vote::isLiked).count();
                        int dislikes = (int) post.getVotes().stream().filter(v -> !v.isLiked()).count();
                        return new PostDetailDTO(post.getId(), post.getTitle(), post.getContent(),
                                        post.getMediaType(), post.getMediaUrl(), userSummary,
                                        likes, dislikes, post.getComments().size(), post.getCreatedAt(),
                                        post.isBanned());
                });
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
                                user.isAdmin(),
                                user.isBanned());
        }

        @Override
        public Page<PostDetailDTO> getReportedPosts(int page, long userId) {
                System.out.println("the user i: " + userId + " the page: " + page);
                int size = 10;
                Pageable pageable = PageRequest.of(
                                page,
                                size,
                                Sort.by(Sort.Direction.DESC, "createdAt"));
                Page<Post> posts = this.reportRepository.findAllReportedPosts(pageable, userId);
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
                                        post.getCreatedAt(),
                                        post.isBanned());
                });
        }

        @Override
        @Transactional
        public boolean banPost(long id) {

                Optional<Post> optionalPost = postRepository.findById(id);

                if (optionalPost.isEmpty()) {
                        return false; // post not found → frontend shows error
                }

                Post post = optionalPost.get();

                // If already banned, you may decide what to do
                if (post.isBanned()) {
                        return true; // already banned → still success from UI POV
                }
                this.reportRepository.updateStatusByPostId(post.getId(), ReportStatus.APPROVED);

                post.setBanned(true);
                postRepository.save(post); // explicit save = clearer intent

                return true;
        }

        /**
         * @Transactional
         *                Spring does NOT call the method directly.
         * 
         *                Instead, Spring creates a proxy around your service and wraps
         *                the method call like this:
         * 
         *                Pseudo-flow:
         *                open DB transaction
         *                ↓
         *                call unbanPost()
         *                ↓
         *                if no exception → COMMIT
         *                if exception → ROLLBACK
         *                and it goes like:
         * 
         *                BEGIN TRANSACTION;
         *                SELECT * FROM post WHERE id = ?
         *                UPDATE post SET banned = false WHERE id = ?
         *                COMMIT;
         */

        @Override
        @Transactional
        public boolean unbanPost(long id) {

                Optional<Post> optionalPost = postRepository.findById(id);

                if (optionalPost.isEmpty()) {
                        return false; // post not found → frontend shows error
                }

                Post post = optionalPost.get();

                // If already banned, you may decide what to do
                if (!post.isBanned()) {
                        return true; // already banned → still success from UI POV
                }

                this.reportRepository.updateStatusByPostId(post.getId(), ReportStatus.REJECTED);

                post.setBanned(false);
                postRepository.save(post); // explicit save = clearer intent

                return true;
        }

        @Override
        public boolean deletePost(long postId) {
                if (!this.postRepository.existsById(postId)) {
                        return false;
                }
                this.postRepository.deleteById(postId);
                return true;
        }
}