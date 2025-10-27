package com.blog.backend.dto;

import lombok.Data;

@Data // Lombok annotation to generate getters, setters, toString, etc.
// this the data structure frontend sends with server controlled data flow.
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatar;
    private String nickname;
    private String dateOfBirth;
    private boolean isAdmin;

    // without lambok, you would need to manually add getters and setters
    // like getdateOfBirth, setDateOfBirth, etc.
    // getDateOfBirth() { return dateOfBirth; }
    // setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
}