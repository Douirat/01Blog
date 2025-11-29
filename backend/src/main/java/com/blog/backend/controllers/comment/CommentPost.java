package com.blog.backend.controllers.comment;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentPost {

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(@Valid @RequestBody CommentRequest request) {
        // Save the comment
        Comment saved = commentService.save(request);

        // Return only the necessary info
        CommentResponseDTO response = new CommentResponseDTO(
                saved.getId(),
                true,
                "Comment created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}