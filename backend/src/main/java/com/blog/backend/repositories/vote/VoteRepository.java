package com.blog.backend.repositories.vote;


import com.blog.backend.models.vote.Vote;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByPostId(Long postId);
    List<Vote> findByUserId(Long userId);
    Optional<Vote> findById(Long id);
}
