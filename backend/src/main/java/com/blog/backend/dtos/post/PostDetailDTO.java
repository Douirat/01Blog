package com.blog.backend.dtos.post;

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
        String createdAt) {
}