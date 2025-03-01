package com.example.vaidya.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.example.vaidya.entity.User;

/**
 * 📌 UserService Interface
 * - Defines the contract for user-related operations.
 * - Includes methods for CRUD operations, pagination, and filtering.
 */
public interface UserService {

    /**
     * ✅ Retrieve a list of all users.
     * @return List of all User entities.
     */
    List<User> getAllUsers();

    /**
     * ✅ Retrieve a user by their unique ID.
     * @param id The unique identifier of the user.
     * @return An Optional containing the User if found, or empty otherwise.
     */
    Optional<User> getUserById(Long id);

    /**
     * ✅ Update an existing user.
     * @param id The unique identifier of the user to be updated.
     * @param userDetails The updated user details.
     * @return The updated User entity.
     */
    User updateUser(Long id, User userDetails);

    /**
     * ✅ Delete a user by their ID.
     * @param id The unique identifier of the user to be deleted.
     */
    void deleteUser(Long id);

    /**
     * ✅ Retrieve paginated list of users.
     * @param page The page number (zero-based index).
     * @param size The number of records per page.
     * @return A paginated list of users.
     */
    Page<User> getUsers(int page, int size);

    /**
     * ✅ Filter users based on dynamic criteria.
     * @param filters A map containing key-value pairs for filtering users.
     * @return A list of filtered User entities.
     */
    List<User> filterUserss(Map<String, Object> filters);
    
    List<User> getAllUsersSorted(String sortBy, String sortDirection);

    // ❌ (Commented) Alternative filtering method with specific parameters.
    // List<User> filterUsers(Long userId, String fullName, String userEmail, String address, Integer roleId);
}
