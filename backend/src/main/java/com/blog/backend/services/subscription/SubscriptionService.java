package com.blog.backend.services.subscription;

import com.blog.backend.models.subscriptions.Subscription;

public interface SubscriptionService {
    Subscription subscribe(long followerId, long followedId);

    boolean isFollowing(long followerId, long followedId);
}
