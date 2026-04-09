package com.blog.backend.controllers.subscription;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<Map<String, String>> follow(@RequestParam long followedId) {

        Long followerId = this.getUserIdFromContext();

        try {
            Subscription sub = subscriptionService.subscribe(followerId, followedId);
            return ResponseEntity.ok(
                    Map.of(
                            "status", "success",
                            "message", "Subscription created with id " + sub.getId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(
                            Map.of(
                                    "status", "error",
                                    "message", "Already following this user"));
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