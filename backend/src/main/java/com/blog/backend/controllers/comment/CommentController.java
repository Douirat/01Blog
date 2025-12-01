package com.blog.backend.controllers.comment;

import com.blog.backend.dtos.comment.CommentResponseDTO;
import com.blog.backend.services.comment.CommentService;
import com.blog.backend.dtos.comment.CommentDTO;

import org.springframework.security.core.context.SecurityContextHolder;
import com.blog.backend.security.PrincipalUser;
import jakarta.validation.Valid;
import com.blog.backend.models.comment.Comment;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

      private final CommentService commentService;


    /**
     * 
     * @param request payload: {
     *                "title": "my comment",
     *                "content": "test body",
     *                "postId": 1
     *                }
     * 
     * @return comment_id
     */
    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(@Valid @RequestBody CommentDTO request) {
        PrincipalUser currentUser = (PrincipalUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        // Save the comment
        Comment saved = commentService.createComment(request, currentUser.getId());

        // Return only the necessary info
        CommentResponseDTO response = new CommentResponseDTO(
                saved.getId(),
                true,
                "Comment created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}