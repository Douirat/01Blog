package com.blog.backend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import com.blog.backend.dto.post.PostInputDTO;
import com.blog.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import com.blog.backend.util.JwtUtil;
import org.springframework.web.bind.annotation.RequestHeader;
import com.blog.backend.model.Post;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import java.lang.RuntimeException;
import com.blog.backend.dto.post.PostDTO;
import java.lang.Exception;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createPost(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @ModelAttribute PostInputDTO dto) {

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing or invalid Authorization header");
            }

            // Use your util method
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserId(token);

            Post saved = postService.createPost(userId, dto);
            return ResponseEntity.ok(saved);

        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error: " + e.getMessage());
        }
    }

    // Get all the posts component:
    @GetMapping
    public ResponseEntity<Page<PostDTO>> getPosts(
            @RequestParam(defaultValue = "0") int page) {

        if (page < 0) {
            return ResponseEntity.badRequest().build();
        }

        Page<PostDTO> posts = postService.getPosts(page);

        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(posts);
    }

}
