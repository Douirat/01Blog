package com.blog.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.blog.backend.model.Comment;
import java.util.List;
import com.blog.backend.model.Post;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long id);
    List<Comment> findByPostId(Long postId);
    List<Comment> findByUserId(Long userId);
}