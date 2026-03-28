package com.blog.backend.dtos.post;

import java.time.LocalDateTime;

public record PostDetailDTO(
        Long id,
        String title,
        String content,
        String mediaType,
        String mediaUrl,
        UserSummaryDTO user,
        Integer likes,
        Integer dislikes,
        Integer commentsCount,
        LocalDateTime createdAt,
        boolean isBanned
) {
}