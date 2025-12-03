package com.blog.backend.dtos.comment;

// The representer record for the comment
public record CommentDetialsDTO(
    int id,
    String content,
    String createdAt,
    CommentAuthor author
) {}
