package com.blog.backend.models.comment;

import java.lang.annotation.Inherited;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.blog.backend.models.post.Post;
import com.blog.backend.models.user.User;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Entity

@Table(name = "comments")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // the commenter

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    public Comment() {}
}
