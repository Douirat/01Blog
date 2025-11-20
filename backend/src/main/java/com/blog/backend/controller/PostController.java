package com.blog.backend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import com.blog.backend.dto.post.PostInputDTO;
import com.blog.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import com.blog.backend.util.JwtUtil;
import org.springframework.web.bind.annotation.RequestHeader;
import com.blog.backend.model.Post;
import org.springframework.web.bind.annotation.ModelAttribute;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import java.lang.RuntimeException;
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

        // System.out.println("Received PostInputDTO ----------------WWW.fuck.com: " + dto);
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

}
