package com.blog.backend.controllers.profile;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.blog.backend.services.profile.ProfileService;
import com.blog.backend.dtos.user.PaginatedUsersDTO;
import com.blog.backend.dtos.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import java.util.Optional;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     * 
     * @param page
     * @return paginated users based on a search.
     */
    @GetMapping("/search")
    public ResponseEntity<PaginatedUsersDTO> getProfilesOnSearch(@RequestParam(defaultValue = "0") int page,
            @RequestParam String value) {
        if (value == null || value.isEmpty() || page < 0) {
            PaginatedUsersDTO errorResponse = new PaginatedUsersDTO(
                    List.of(), // empty content
                    true, // last page
                    0, // total pages
                    0 // total elements
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
        System.out.println("im fucking this");

        Page<UserDTO> users = profileService.fetchUsersContains(page, value);

        PaginatedUsersDTO response = new PaginatedUsersDTO(
                users.getContent(),
                users.isLast(),
                users.getTotalPages(),
                users.getTotalElements());

        return ResponseEntity.ok(response);

    }

    /**
     * Get lists of users to allow serching and rendring users' profiles:
     */
    @GetMapping
    public ResponseEntity<PaginatedUsersDTO> getProfiles(@RequestParam(defaultValue = "0") int page) {

        if (page < 0) {
            PaginatedUsersDTO errorResponse = new PaginatedUsersDTO(
                    List.of(), // empty content
                    true, // last page
                    0, // total pages
                    0 // total elements
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Page<UserDTO> users = profileService.fetchUsers(page);

        PaginatedUsersDTO response = new PaginatedUsersDTO(
                users.getContent(),
                users.isLast(),
                users.getTotalPages(),
                users.getTotalElements());

        return ResponseEntity.ok(response);
    }

    /**
     * Get a specific user's profile by user id:
     */
    @GetMapping("/user")
    public ResponseEntity<UserDTO> getUserProfile(@RequestParam Long userId) {
        return Optional.ofNullable(profileService.fetchUserProfile(userId))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/ban")
    public ResponseEntity<Map<String, String>> banUser(@RequestParam long id) {

        if (id == 1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Admin ban not allowed"));
        }

        boolean banned = this.profileService.banUser(id);

        if (!banned) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }

        return ResponseEntity.ok(Map.of("message", "User banned successfully"));
    }

    @PatchMapping("/unban")
    public ResponseEntity<Map<String, String>> activateUserAccount(@RequestParam long id) {

        if (id == 1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Admin account not allowed"));
        }

        boolean banned = this.profileService.activateUserAccount(id);

        if (!banned) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }

        return ResponseEntity.ok(Map.of("message", "User account activated successfully"));
    }

    

}