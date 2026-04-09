package com.blog.backend.models.subscriptions;

import com.blog.backend.models.user.User;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "subscriptions",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"followerId", "followedId"}
    )
)
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followerId", nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followedId", nullable = false)
    private User followed;
}