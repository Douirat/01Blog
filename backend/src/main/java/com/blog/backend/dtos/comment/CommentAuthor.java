package com.blog.backend.dtos.comment;

// The representer record for the comment author
public record CommentAuthor(
    int id,
    String nickName
) {}