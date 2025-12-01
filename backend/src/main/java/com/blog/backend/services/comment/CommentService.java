package com.blog.backend.services.comment;


import com.blog.backend.dtos.comment.CommentDTO;
import com.blog.backend.models.comment.Comment;


public interface CommentService {
 Comment createComment(CommentDTO requestDTO, Long userId);
}