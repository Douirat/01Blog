package com.blog.backend.controllers.post;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.blog.backend.dtos.post.PostInputDTO;
import com.blog.backend.dtos.user.UserDTO;
import com.blog.backend.services.post.PostService;
import lombok.RequiredArgsConstructor;
import com.blog.backend.models.post.Post;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import com.blog.backend.dtos.post.PostDetailDTO;
import com.blog.backend.dtos.post.PaginatedPostsDTO;
import com.blog.backend.security.PrincipalUser;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createPost(
            @ModelAttribute PostInputDTO dto) {

        dto.setTitle(dto.getTitle().trim());
        dto.setContent(dto.getContent().trim());
        if (dto.getTitle() == null || dto.getTitle().isEmpty() || dto.getContent() == null
                || dto.getContent().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Long user_id = this.getUserIdFromContext();
        Post saved = postService.createPost(user_id, dto);
        return ResponseEntity.ok(saved);
    }

    /**
     * Update an existing post:
     */
    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId,
            @ModelAttribute PostInputDTO dto) {
        dto.setTitle(dto.getTitle().trim());
        dto.setContent(dto.getContent().trim());
        if (dto.getTitle() == null || dto.getTitle().isEmpty() || dto.getContent() == null
                || dto.getContent().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Long userId = this.getUserIdFromContext();

        Post saved = postService.updatePost(userId, postId, dto);

        return ResponseEntity.ok(saved);
    }

    // Get all the posts component:
    @GetMapping
    public ResponseEntity<PaginatedPostsDTO> getPosts(
            @RequestParam(defaultValue = "0") int page) {
        if (page < 0) {
            return ResponseEntity.badRequest().build();
        }

        // get the user id (principal user) from the context:
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        // 1. Check if the principal is our expected user type and is authenticated
        if (!(principal instanceof PrincipalUser)) {
            // This happens if the user is anonymous or not authenticated properly
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // 2. Cast the object and extract the userId
        PrincipalUser currentUser = (PrincipalUser) principal;
        Long userId = currentUser.getId();

        Page<PostDetailDTO> posts = postService.getAllPosts(userId, page);

        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        PaginatedPostsDTO response = new PaginatedPostsDTO(
                posts.getContent(),
                posts.isLast(),
                posts.getTotalPages(),
                posts.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<PaginatedPostsDTO> getAllPostsForAdmin(
            @RequestParam(defaultValue = "0") int page) {
        if (page < 0)
            return ResponseEntity.badRequest().build();
        Page<PostDetailDTO> posts = postService.getAllPostsForAdmin(page);
        if (posts.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(new PaginatedPostsDTO(
                posts.getContent(), posts.isLast(),
                posts.getTotalPages(), posts.getTotalElements()));
    }

    // get posts for a specific user.
    @GetMapping("/profile")
    public ResponseEntity<PaginatedPostsDTO> getUserPosts(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page) {

        if (page < 0) {
            return ResponseEntity.badRequest().build();
        }

        System.out.println("postId: " + userId);
        System.out.println("page: " + page);

        // Long user_id = this.getUserIdFromContext();
        Page<PostDetailDTO> posts = postService.getUserPosts(page, userId);

        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        PaginatedPostsDTO response = new PaginatedPostsDTO(
                posts.getContent(),
                posts.isLast(),
                posts.getTotalPages(),
                posts.getTotalElements());

        return ResponseEntity.ok(response);

    }

    private Long getUserIdFromContext() {
        PrincipalUser currentUser = (PrincipalUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return currentUser.getId();
    }

    /**
     * Get the user by postId:
     */
    @GetMapping("/user")
    public ResponseEntity<UserDTO> getUserByPostId(@RequestParam("postId") long postId) {
        UserDTO user = this.postService.getUserByPostId(postId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/reports")
    public ResponseEntity<PaginatedPostsDTO> getReportedPosts(@RequestParam("page") int page,
            @RequestParam("userId") long userId) {
        if (page < 0) {
            return ResponseEntity.badRequest().build();
        }
        Page<PostDetailDTO> posts = postService.getReportedPosts(page, userId);

        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        PaginatedPostsDTO response = new PaginatedPostsDTO(
                posts.getContent(),
                posts.isLast(),
                posts.getTotalPages(),
                posts.getTotalElements());
        return ResponseEntity.ok(response);
    }

    /**
     * 
     * @param id
     *           the frontend will be handled normally using:
     *           return this.http.patch(this.baseUrl, {params});
     * @return the ok response is a boolean in it self
     */

    @PatchMapping("/ban")
    public ResponseEntity<Map<String, String>> banPost(@RequestParam long id) {
        boolean banned = postService.banPost(id);

        if (!banned) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Post not found"));
        }

        return ResponseEntity.ok(Map.of("message", "Post banned successfully"));
    }

    @PatchMapping("/unban")
    public ResponseEntity<Map<String, String>> unbanPost(@RequestParam long id) {
        boolean banned = postService.unbanPost(id);

        if (!banned) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Post not found"));
        }

        return ResponseEntity.ok(Map.of("message", "Post unbanned successfully"));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deletePost(@RequestParam long postId) {
        boolean deleted = this.postService.deletePost(postId);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error deleting post"));
        }
        return ResponseEntity.ok(Map.of("message", "Post deleted successfully"));
    }
}