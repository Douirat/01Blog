package com.blog.backend.controllers.vote;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.blog.backend.repositories.vote.VoteRepository;
import com.blog.backend.services.vote.VoteService;
import com.blog.backend.dtos.vote.VoteRequestDTO;
import com.blog.backend.dtos.vote.VoteResponseDTO;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import com.blog.backend.security.PrincipalUser;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;



    @PostMapping
    public ResponseEntity<VoteResponseDTO> castVote(@RequestBody VoteRequestDTO vote) {

        // Use a single variable name for clarity
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        // 1. Check if the principal is our expected user type and is authenticated
        if (!(principal instanceof PrincipalUser)) {
            // This happens if the user is anonymous or not authenticated properly
            VoteResponseDTO errorResponse = new VoteResponseDTO(null, false, "User not authenticated");
            return ResponseEntity.status(401).body(errorResponse);
        }

        // 2. Cast the object and extract the userId
        PrincipalUser currentUser = (PrincipalUser) principal;
        Long userId = currentUser.getId(); // Assuming 'getId()' exists in your PrincipalUser class

        // 3. Call the service layer with the retrieved IDs
        // Assuming 'true' for a default 'like' vote
        vote.setUserId(userId); // add userId into DTO
        VoteResponseDTO responseDTO = voteService.toggleVote(vote);
        try {

            // Return 200 OK with the success DTO
            return ResponseEntity.ok(responseDTO);

        } catch (RuntimeException e) {
            // Handle specific business logic errors (e.g., Post Not Found)
            VoteResponseDTO errorResponse = new VoteResponseDTO(null, false,
                    "Error processing vote: " + e.getMessage());
            // Return 400 Bad Request with the error DTO
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
