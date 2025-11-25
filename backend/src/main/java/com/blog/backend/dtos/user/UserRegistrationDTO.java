package com.blog.backend.dtos.user;

import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegistrationDTO {

    private String email;
    private String password;          // ✅ add this
    private String firstName;
    private String lastName;
    private String nickname;
    private LocalDate dateOfBirth;
    private Boolean isAdmin;
    private MultipartFile avatar;     // optional
}
