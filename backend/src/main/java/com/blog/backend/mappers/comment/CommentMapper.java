package com.blog.backend.mappers.comment;

import com.blog.backend.dtos.comment.CommentDetailsDTO;
import com.blog.backend.models.comment.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "author.id", source = "user.id")
    @Mapping(target = "author.nickName", source = "user.nickname")
    CommentDetailsDTO toDto(Comment comment);
}