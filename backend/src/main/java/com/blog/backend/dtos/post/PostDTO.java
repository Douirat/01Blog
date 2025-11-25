package com.blog.backend.dto.post;

import java.time.LocalDateTime;
import java.util.List;
import com.blog.backend.dto.comment.CommentDTO;
import lombok.Data;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private String mediaType;
    private String mediaUrl;
    private Long userId;
    private LocalDateTime createdAt;
    private int likes;
    private int dislikes;
    // Getters and Setters
}
