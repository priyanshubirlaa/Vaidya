package com.spring.vaidya.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.vaidya.entity.User;

/**
 * Repository interface for managing User entities.
 * Extends JpaRepository to provide built-in CRUD operations.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Checks if a user exists with the given email.
     *
     * @param email The email to check.
     * @return true if a user exists, false otherwise.
     */
    Boolean existsByUserEmail(String email);

    /**
     * Finds a user by their email (case-sensitive).
     *
     * @param email The email to search for.
     * @return The user associated with the given email.
     */
    User findByUserEmail(String email);

    /**
     * Finds a user by their email (case-insensitive).
     *
     * @param email The email to search for (ignoring case).
     * @return An Optional containing the user if found, empty otherwise.
     */
    Optional<User> findByUserEmailIgnoreCase(String email);

    /**
     * Finds a user by their full name.
     *
     * @param fullName The full name of the user.
     * @return An Optional containing the user if found, empty otherwise.
     */
    Optional<User> findByFullName(String fullName);
}
