package com.blog.backend.model;

import java.time.LocalDate;
// import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth; // or LocalDate if you want date type


    @Column(nullable = true)
    private String avatar;

    @Column(nullable = true)
    private String nickname;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false; // default is normal user

    // constructor
    public User() {
    }

    // Optional constructor for convenience
    public User(String email, String password, String firstName, String lastName,
            LocalDate dateOfBirth, String avatar, String nickname, boolean isAdmin) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.avatar = avatar;
        this.nickname = nickname;
        this.isAdmin = isAdmin;
    }
}
/*
 * When you puth the annotations @Getter/Setter from Lombok library, you dont need to write the getters/setters manually.
 * Lombok will generate them at compile time.
 * For more information, visit: https://projectlombok.org/features/GetterSetter
 * summary: this class represents the User entity in the database with fields like id, email, password, firstName, lastName,
 *  dateOfBirth, avatar, nickname, and isAdmin.
 * Each field is mapped to a corresponding column in the "users" table.
 * and includes constructors for creating User instances.
 * and the setters and getters like getId(), setId(Long id), getEmail(), setEmail(String email), etc.
 * are generated automatically by Lombok.
  */