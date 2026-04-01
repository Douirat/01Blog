package com.blog.backend.repositories.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blog.backend.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
// import java.util.*;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<User> searchByName(@Param("keyword") String keyword, Pageable pageable);
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