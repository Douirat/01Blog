package com.blog.backend.models.vote;

import com.blog.backend.models.user.User;
import com.blog.backend.models.post.Post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
// Removed @Data

@Entity
@Table(name = "votes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"post_id", "user_id"})
})
@Getter
@Setter
@NoArgsConstructor // For JPA compliance
@AllArgsConstructor // Optional, for convenience in creating instances
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    // You might want @ToString.Exclude here if using @ToString explicitly
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    // You might want @ToString.Exclude here if using @ToString explicitly
    private User user;

    // true = like, false = dislike
    private boolean liked = true;
    
}
