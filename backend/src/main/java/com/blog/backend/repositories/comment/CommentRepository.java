package com.blog.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.blog.backend.models.comment.Comment;

import java.util.List;
import com.blog.backend.models.post.Post;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long id);
    List<Comment> findByPostId(Long postId);
    List<Comment> findByUserId(Long userId);
}