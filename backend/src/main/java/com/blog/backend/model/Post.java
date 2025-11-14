package com.blog.backend.model;

import java.lang.annotation.Inherited;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // coresponds to Go's int64

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    // optional fields:
    private String mediaType;
    private String mediaUrl;

    // many to one relationship with the users' table:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // the creatng time will be added automatically the momoent of creating:
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Cascade comments
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    // Cascade votes
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes;

    /*
     * create a an empty constructor to ease the JPA ORM operations
     */
    public Post() {
    }

    // Optional constructor for convenience:
    public Post(String title, String content, String mediaType, String mediaUrl, User user, LocalDate createdAt) {
        this.title = title;
        this.content = content;
        this.mediaType = mediaType;
        this.mediaUrl = mediaUrl;
        this.user = user;
        this.createdAt = createdAt;
    }
}
// Posts

// Users can create/edit/delete posts with media (image or video) and text
// Each post includes a timestamp, description, and media preview
// Other users can like and comment on posts