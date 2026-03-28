package com.blog.backend.controllers.user;

import com.blog.backend.dtos.user.LoginRequestDTO;
import com.blog.backend.dtos.user.UserRegistrationDTO;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// This maps the http request data to a java object.
import org.springframework.web.bind.annotation.*;
import com.blog.backend.dtos.user.AuthResponseDTO;
import com.blog.backend.services.user.UserService;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/users")
public class UserController {
        private final UserService userService;

        /**
         * Constructor injection of UserService.
         * 
         * @param userService - Service handling user authentication logic
         */
        public UserController(UserService userService) {
                this.userService = userService;
        }

        /**
         * Registers a new user account.
         * 
         * @param user - User registration data (validated)
         * @return ResponseEntity<AuthResponseDTO>
         *         - 200 OK: Registration successful with JWT token
         *         - 400 Bad Request: Validation failed or user already exists
         *         - 500 Internal Server Error: Server-side error
         * 
         *         Added @Valid annotation to trigger validation on User model.
         *         Consider creating RegisterRequestDTO for better separation of
         *         concerns.
         */
        @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<AuthResponseDTO> registerUser(@Valid @ModelAttribute UserRegistrationDTO user) {
                return userService.registerUser(user)
                                .map(registeredUser -> ResponseEntity
                                                .status(HttpStatus.CREATED)
                                                .body(registeredUser))
                                .orElseGet(() -> ResponseEntity
                                                .badRequest()
                                                .build());
        }

        /*
         * const formData = new FormData();
         * formData.append("email", user.email);
         * formData.append("password", user.password);
         * formData.append("firstName", user.firstName);
         * formData.append("lastName", user.lastName);
         * formData.append("nickname", user.nickname);
         * formData.append("dateOfBirth", user.dateOfBirth); // ISO string
         * if (user.avatar) formData.append("avatar", user.avatar);
         * 
         * fetch("/api/register", {
         * method: "POST",
         * body: formData
         * });
         * 
         */

        /**
         * Authenticates a user and returns JWT token.
         * 
         * @param payload - Login credentials (email and password)
         * @return ResponseEntity<AuthResponseDTO>
         *         - 200 OK: Authentication successful with JWT token
         *         - 401 Unauthorized: Invalid credentials
         *         - 400 Bad Request: Validation failed
         *         Validation is performed automatically via @Valid annotation.
         */

        @PostMapping("/login")
        public ResponseEntity<AuthResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO payload) {
                return userService.loginUser(payload)
                                .map(authResponse -> ResponseEntity
                                                .ok()
                                                .body(authResponse))
                                .orElseGet(() -> ResponseEntity
                                                .status(HttpStatus.UNAUTHORIZED)
                                                .build());
        }

        /**
         * Logs out the current user.
         * 
         * In a stateless JWT setup, logout is handled client-side by deleting the
         * token.
         * This endpoint exists for:
         * 1. Consistency in API design
         * 2. Future server-side token blacklisting if needed
         * 3. Logging/auditing purposes
         * 
         * @return ResponseEntity<Void>
         *         - 200 OK: Logout acknowledged
         * 
         *         To implement server-side token invalidation:
         *         - Add token blacklist (Redis recommended for TTL support)
         *         - Extract token from Authorization header
         *         - Add token to blacklist with expiry matching token TTL
         */
        @DeleteMapping("/logout")
        public ResponseEntity<Void> logoutUser() {
                // In a stateless JWT setup, logout is typically handled on the client
                // by deleting the token. Here, we just return a success response.
                return ResponseEntity
                                .noContent()
                                .build();
        }

        /**
         * Verifies JWT token validity and returns current user data.
         * 
         * This endpoint is called by the frontend to:
         * 1. Verify token is still valid on page refresh
         * 2. Get updated user data
         * 3. Check authentication status before accessing protected routes
         * 
         * @param authHeader - Authorization header containing "Bearer <token>"
         * @return ResponseEntity<AuthResponseDTO>
         *         - 200 OK: Token valid, returns user data
         *         - 401 Unauthorized: Token invalid/expired/missing
         * 
         *         Note: Token extraction could be handled by @AuthenticationPrincipal
         *         if using Spring Security's authentication context.
         * 
         *         Alternative approach using Spring Security:
         * @GetMapping
         *             public ResponseEntity<AuthResponseDTO> checkStatus(
         * @AuthenticationPrincipal UserDetails userDetails) {
         *                          return
         *                          userService.getUserData(userDetails.getUsername())
         *                          .map(ResponseEntity::ok)
         *                          .orElse(ResponseEntity.status(401).build());
         *                          }
         */
        @GetMapping
        public ResponseEntity<AuthResponseDTO> checkStatus(@RequestHeader("Authorization") String authHeader) {
                return userService.checkStatus(authHeader.replace("Bearer ", ""))
                                .map(authResponse -> ResponseEntity
                                                .ok()
                                                .body(authResponse))
                                .orElseGet(() -> ResponseEntity
                                                .status(HttpStatus.UNAUTHORIZED)
                                                .build());
                /**
                 * Global exception handler for this controller.
                 * Catches validation errors and other exceptions to return consistent error
                 * responses.
                 * Consider moving to a @ControllerAdvice class for application-wide error
                 * handling.
                 */
                // @ExceptionHandler(MethodArgumentNotValidException.class)
                // public ResponseEntity<ErrorResponseDTO> handleValidationErrors(
                // MethodArgumentNotValidException ex) {
                // Map<String, String> errors = new HashMap<>();
                // ex.getBindingResult().getFieldErrors()
                // .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
                //
                // return ResponseEntity
                // .badRequest()
                // .body(new ErrorResponseDTO("Validation failed", errors));
                // }
        }


}