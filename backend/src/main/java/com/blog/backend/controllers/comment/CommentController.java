package com.blog.backend.controllers.comment;


import com.blog.backend.services.comment.CommentService;
import com.blog.backend.dtos.comment.CommentDTO;
import com.blog.backend.dtos.comment.CommentDetailsDTO;
import com.blog.backend.dtos.comment.PaginatedCommentsDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import com.blog.backend.security.PrincipalUser;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
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
        public ResponseEntity<CommentDetailsDTO> createComment(@Valid @RequestBody CommentDTO request) {
                PrincipalUser currentUser = (PrincipalUser) SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal();
                // Save the comment
                CommentDetailsDTO saved = commentService.createComment(request, currentUser.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        }

        @GetMapping
        public ResponseEntity<Object> getComments(@RequestParam Long postId,
                        @RequestParam(defaultValue = "0") int page) {
                // Add validation for the postId if needed (e.g., must be > 0)
                if (postId == null || postId <= 0) {
                        return ResponseEntity.badRequest().body("A valid postId is required.");
                }
                if (page < 0) {
                        return ResponseEntity.badRequest().body("Page number cannot be negative.");
                }
                Page<CommentDetailsDTO> comments = commentService.getPostComments(page, postId);
                if (comments.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                PaginatedCommentsDTO response = new PaginatedCommentsDTO(
                                comments.getContent(),
                                comments.isLast(),
                                comments.getTotalPages(),
                                comments.getTotalElements());

                return ResponseEntity.ok(response);
        }

}