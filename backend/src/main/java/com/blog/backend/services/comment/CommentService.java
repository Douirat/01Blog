package com.blog.backend.services.comment;

import com.blog.backend.dtos.comment.CommentDTO;
import com.blog.backend.models.comment.Comment;
import org.springframework.data.domain.Page;
import com.blog.backend.dtos.comment.CommentDetailsDTO;

public interface CommentService {
    CommentDetailsDTO createComment(CommentDTO requestDTO, Long userId);

    Page<CommentDetailsDTO> getPostComments(int page, Long postId);
}