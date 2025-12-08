package com.blog.backend.controllers.profile;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;

import com.blog.backend.services.profile.ProfileService;
import com.blog.backend.dtos.user.PaginatedUsersDTO;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     * Get lists of users to allow serching and rendring users' profiles:
     */
    @GetMapping
    public ResponseEntity<PaginatedUsersDTO> getProfiles(@RequestParam(defaultValue = "0") int page) {

        if (page < 0) {
            return ResponseEntity.badRequest().build();
        }

        Page<PaginatedUsersDTO> users = profileService.fetchUsers(page);

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        PaginatedUsersDTO response = new PaginatedUsersDTO(
                users.getContent(),
                users.isLast(),
                users.getTotalPages(),
                users.getTotalElements());

        return ResponseEntity.ok(response);
    }

}