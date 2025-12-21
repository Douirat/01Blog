package com.blog.backend.controllers.post;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import com.blog.backend.dtos.post.PostInputDTO;
import com.blog.backend.services.post.PostService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestHeader;
import com.blog.backend.models.post.Post;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import java.lang.RuntimeException;
import java.lang.Exception;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        @ModelAttribute PostInputDTO dto
) {
    Long userId = this.getUserIdFromContext();

    System.out.println("postId: " + postId + ", user id: " + userId);

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

        Page<PostDetailDTO> posts = postService.getAllPosts(page);

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
}