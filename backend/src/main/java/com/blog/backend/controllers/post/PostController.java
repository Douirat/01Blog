package com.blog.backend.controllers.post;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

        PrincipalUser currentUser = (PrincipalUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Post saved = postService.createPost(currentUser.getId(), dto);
        return ResponseEntity.ok(saved);

    }

// Get all the posts component:
@GetMapping
public ResponseEntity<PaginatedUsersDTO> getProfiles(@RequestParam(defaultValue = "0") int page) {

    if (page < 0) {
        PaginatedUsersDTO errorResponse = new PaginatedUsersDTO(
            List.of(), // empty content
            true,      // mark as last page
            0,         // total pages
            0          // total elements
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    Page<UserDTO> users = profileService.fetchUsers(page);

    if (users.isEmpty()) {
        PaginatedUsersDTO emptyResponse = new PaginatedUsersDTO(
            List.of(),
            true,
            users.getTotalPages(),
            users.getTotalElements()
        );
        return ResponseEntity.noContent().body(emptyResponse); 
    }

    PaginatedUsersDTO response = new PaginatedUsersDTO(
        users.getContent(),
        users.isLast(),
        users.getTotalPages(),
        users.getTotalElements()
    );

    return ResponseEntity.ok(response);
}
}
