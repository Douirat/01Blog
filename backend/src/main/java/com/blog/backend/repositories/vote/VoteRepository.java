package com.blog.backend.repositories.vote;

import com.blog.backend.models.post.Post;
import com.blog.backend.models.user.User;
import com.blog.backend.models.vote.Vote;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByPostAndUser(Post post, User user);

    long countByPostAndLikedTrue(Post post);

    long countByPostAndLikedFalse(Post post);

    List<Vote> findByPostId(Long postId);

    List<Vote> findByUserId(Long userId);

    Optional<Vote> findById(Long id);
}
