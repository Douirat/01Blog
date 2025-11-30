package com.blog.backend.services.comment;

import com.blog.backend.dtos.comment.CommentResponseDTO;

public interface CommentService {
 CommentResponseDTO createComment(CommentRequestDTO requestDTO, Long userId);
}