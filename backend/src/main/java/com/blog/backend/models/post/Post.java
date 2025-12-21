package com.blog.backend.models.post;

import com.blog.backend.models.comment.Comment;
import com.blog.backend.models.user.User;
import com.blog.backend.models.vote.Vote;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // ⬅ add this line


import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String mediaType;
    private String mediaUrl;

    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "user_id", nullable = false)
    // private User user;

    @ManyToOne(fetch = FetchType.LAZY) // ⬅ changed from EAGER → LAZY
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vote> votes = new HashSet<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
