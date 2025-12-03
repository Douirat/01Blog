package com.blog.backend.dtos.comment;

// The representer record for the comment
public record CommentDetailsDTO(
    int id,
    String content,
    String createdAt,
    CommentAuthor author
) {}
