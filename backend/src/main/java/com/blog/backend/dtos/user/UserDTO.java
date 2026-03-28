package com.blog.backend.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data // Lombok annotation to generate getters, setters, toString, etc.
// this the data structure frontend sends with server controlled data flow.
@AllArgsConstructor // generates constructor with all fields as parameters.
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatar;
    private String nickname;
    private LocalDate dateOfBirth;
    @JsonProperty("isAdmin")
    private boolean isAdmin;
    private boolean isBanned;

    // Constructor that maps User -> UserDTO
    // public UserDTO(User user) {
    //     this.id = user.getId();
    //     this.email = user.getEmail();
    //     this.firstName = user.getFirstName();
    //     this.lastName = user.getLastName();
    //     this.avatar = user.getAvatar();
    //     this.nickname = user.getNickname();
    //     this.dateOfBirth = user.getDateOfBirth();
    //     this.isAdmin = user.isAdmin();
    // }

    // without lambok, you would need to manually add getters and setters
    // like getdateOfBirth, setDateOfBirth, etc.
    // getDateOfBirth() { return dateOfBirth; }
    // setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

}