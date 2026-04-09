package com.blog.backend.repositories.subscription;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.backend.models.subscriptions.Subscription;
import com.blog.backend.models.user.User;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    boolean existsByFollowerAndFollowed(User follower, User followed);
}