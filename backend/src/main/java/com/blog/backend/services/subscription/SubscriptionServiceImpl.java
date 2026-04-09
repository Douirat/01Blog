package com.blog.backend.services.subscription;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.blog.backend.models.subscriptions.Subscription;
import com.blog.backend.models.user.User;
import com.blog.backend.repositories.subscription.SubscriptionRepository;
import com.blog.backend.repositories.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public Subscription subscribe(long followerId, long followedId) {

        if (followerId == followedId) {
            throw new IllegalArgumentException("User cannot follow themselves");
        }

        User follower = userRepository.getReferenceById(followerId);
        User followed = userRepository.getReferenceById(followedId);

        Subscription subscription = new Subscription();
        subscription.setFollower(follower);
        subscription.setFollowed(followed);

        return subscriptionRepository.save(subscription);
    }

    @Override
    public boolean isFollowing(long followerId, long followedId) {
        if (followerId == followedId) {
            throw new IllegalArgumentException("User cannot follow themselves");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower not found"));
        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new IllegalArgumentException("Followed user not found"));

        return subscriptionRepository.existsByFollowerAndFollowed(follower, followed);
    }
}