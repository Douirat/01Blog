package com.blog.backend.repositories.subscription;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.backend.models.subscriptions.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {}