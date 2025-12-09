package com.blog.backend.controllers.profile;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;

import com.blog.backend.services.profile.ProfileService;
import com.blog.backend.dtos.user.PaginatedUsersDTO;
import com.blog.backend.dtos.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import org.springframework.data.domain.Page;



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
        PaginatedUsersDTO errorResponse = new PaginatedUsersDTO(
            List.of(), // empty content
            true,      // last page
            0,         // total pages
            0          // total elements
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    Page<UserDTO> users = profileService.fetchUsers(page);

    PaginatedUsersDTO response = new PaginatedUsersDTO(
        users.getContent(),
        users.isLast(),
        users.getTotalPages(),
        users.getTotalElements()
    );

    return ResponseEntity.ok(response);
}

}