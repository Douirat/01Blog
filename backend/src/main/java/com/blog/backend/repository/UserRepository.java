package com.blog.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.backend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    boolean existsByEmail(String email);
}

/*
 * At the first glance the user repository looks empty but in spring data jpa
 * it automatically provides basic CRUD operations like save, findById, findAll,
 * deleteById, etc.
 * so you don't need to explicitly define them unless you want custom queries.
 * like the findByEmail method above which is not provided by default.
 * so sprint boot will automatically execute something like
 * 
 * @Query("SELECT u FROM User u WHERE u.email = :email")
 * Optional<User> findByEmail(@Param("email") String email);
 */