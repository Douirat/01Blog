package com.blog.backend.dto.comment;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.AllArgsConstructor;



@AllArgsConstructor
@Data
public class CommentDTO {
    private Long id;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;

    // Getters and Setters
}
