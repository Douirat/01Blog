package com.blog.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // coresponds to Go's int64

    @Column(nullable = false, unique = true)
    private String email; // this will result in a column email within the users table.

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password; // write-only: not serialized in JSON responses

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private String dateOfBirth; // or LocalDate if you want date type

    @Column(nullable = true)
    private String avatar;

    @Column(nullable = true)
    private String nickname;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false; // default is normal user

    // Optional constructor for convenience
    public User(String email, String password, String firstName, String lastName,
            String dateOfBirth, String avatar, String nickname) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.avatar = avatar;
        this.nickname = nickname;
    }
}
