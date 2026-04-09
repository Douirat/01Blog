package com.blog.backend.controllers.subscription;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.backend.models.subscriptions.Subscription;
import com.blog.backend.security.PrincipalUser;
import com.blog.backend.services.subscription.SubscriptionService;

import java.util.*;

@RequestMapping("/api/subscription")
@RestController
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> follow(@RequestParam long followedId) {

        Long followerId = this.getUserIdFromContext();

        try {
            Subscription sub = subscriptionService.subscribe(followerId, followedId);

            Map<String, Object> response = new HashMap<>();
            response.put("followerId", followerId);
            response.put("followedId", followedId);
            response.put("isFollowing", sub != null);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected server error"));
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> checkSubscription(
            @RequestParam Long followedId) {

        Map<String, Object> response = new HashMap<>();
        try {
            Long followerId = this.getUserIdFromContext();

            boolean isFollowing = subscriptionService.isFollowing(followerId, followedId);

            response.put("followerId", followerId);
            response.put("followedId", followedId);
            response.put("isFollowing", isFollowing);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            response.put("error", "Unexpected error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    private Long getUserIdFromContext() {
        PrincipalUser currentUser = (PrincipalUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return currentUser.getId();
    }
}