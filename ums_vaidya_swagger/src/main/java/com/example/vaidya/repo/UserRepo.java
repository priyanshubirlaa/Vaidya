package com.example.vaidya.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.vaidya.entity.User;

/**
 * 📌 User Repository Interface
 * - Provides CRUD operations and custom query methods for the User entity.
 * - Uses Spring Data JPA for database interactions.
 * - Extends JpaRepository for basic CRUD operations.
 * - Extends JpaSpecificationExecutor for advanced filtering queries.
 */
@Repository
public interface UserRepo extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>  {

    // 🔹 Basic Queries

    /**
     * ✅ Find users by their unique userId.
     */
    List<User> findByUserId(Long userId);

    /**
     * ✅ Find users whose full name contains the given input (case-insensitive).
     */
    List<User> findByFullNameContainingIgnoreCase(String fullName);

    /**
     * ✅ Find users whose email contains the given input (case-insensitive).
     */
    List<User> findByUserEmailContainingIgnoreCase(String userEmail);

    /**
     * ✅ Find users whose address contains the given input (case-insensitive).
     */
    List<User> findByAddressContainingIgnoreCase(String address);

    /**
     * ✅ Find users based on their role ID.
     */
    List<User> findByRoleId(Integer roleId);

    // 🔹 Combined Queries (Full Name, Email, and Role ID)

    /**
     * ✅ Find users whose full name and email contain the given input.
     */
    List<User> findByFullNameContainingIgnoreCaseAndUserEmailContainingIgnoreCase(String fullName, String userEmail);

    /**
     * ✅ Find users whose full name contains the given input and have a specific role ID.
     */
    List<User> findByFullNameContainingIgnoreCaseAndRoleId(String fullName, Integer roleId);

    /**
     * ✅ Find users whose email contains the given input and have a specific role ID.
     */
    List<User> findByUserEmailContainingIgnoreCaseAndRoleId(String userEmail, Integer roleId);

    /**
     * ✅ Find users by full name, email, and role ID.
     */
    List<User> findByFullNameContainingIgnoreCaseAndUserEmailContainingIgnoreCaseAndRoleId(
            String fullName, String userEmail, Integer roleId);

    // 🔹 Queries Including Address

    /**
     * ✅ Find users by address and role ID.
     */
    List<User> findByAddressContainingIgnoreCaseAndRoleId(String address, Integer roleId);

    /**
     * ✅ Find users by address and full name.
     */
    List<User> findByAddressContainingIgnoreCaseAndFullNameContainingIgnoreCase(String address, String fullName);

    /**
     * ✅ Find users by address and email.
     */
    List<User> findByAddressContainingIgnoreCaseAndUserEmailContainingIgnoreCase(String address, String userEmail);

    /**
     * ✅ Find users by address, full name, and email.
     */
    List<User> findByAddressContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndUserEmailContainingIgnoreCase(
            String address, String fullName, String userEmail);

    /**
     * ✅ Find users by address, full name, and role ID.
     */
    List<User> findByAddressContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndRoleId(
            String address, String fullName, Integer roleId);

    /**
     * ✅ Find users by address, email, and role ID.
     */
    List<User> findByAddressContainingIgnoreCaseAndUserEmailContainingIgnoreCaseAndRoleId(
            String address, String userEmail, Integer roleId);

    /**
     * ✅ Find users by address, full name, email, and role ID.
     */
    List<User> findByAddressContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndUserEmailContainingIgnoreCaseAndRoleId(
            String address, String fullName, String userEmail, Integer roleId);

    // 🔹 Queries Including User ID

    /**
     * ✅ Find users by userId and full name.
     */
    List<User> findByUserIdAndFullNameContainingIgnoreCase(Long userId, String fullName);

    /**
     * ✅ Find users by userId and email.
     */
    List<User> findByUserIdAndUserEmailContainingIgnoreCase(Long userId, String userEmail);

    /**
     * ✅ Find users by userId and address.
     */
    List<User> findByUserIdAndAddressContainingIgnoreCase(Long userId, String address);

    /**
     * ✅ Find users by userId and role ID.
     */
    List<User> findByUserIdAndRoleId(Long userId, Integer roleId);

    /**
     * ✅ Find users by userId, full name, and email.
     */
    List<User> findByUserIdAndFullNameContainingIgnoreCaseAndUserEmailContainingIgnoreCase(
            Long userId, String fullName, String userEmail);

    /**
     * ✅ Find users by userId, full name, and address.
     */
    List<User> findByUserIdAndFullNameContainingIgnoreCaseAndAddressContainingIgnoreCase(
            Long userId, String fullName, String address);

    /**
     * ✅ Find users by userId, full name, and role ID.
     */
    List<User> findByUserIdAndFullNameContainingIgnoreCaseAndRoleId(
            Long userId, String fullName, Integer roleId);

    /**
     * ✅ Find users by userId, email, and address.
     */
    List<User> findByUserIdAndUserEmailContainingIgnoreCaseAndAddressContainingIgnoreCase(
            Long userId, String userEmail, String address);

    /**
     * ✅ Find users by userId, email, and role ID.
     */
    List<User> findByUserIdAndUserEmailContainingIgnoreCaseAndRoleId(
            Long userId, String userEmail, Integer roleId);

    /**
     * ✅ Find users by userId, address, and role ID.
     */
    List<User> findByUserIdAndAddressContainingIgnoreCaseAndRoleId(
            Long userId, String address, Integer roleId);

    /**
     * ✅ Find users by userId, full name, email, address, and role ID.
     */
    List<User> findByUserIdAndFullNameContainingIgnoreCaseAndUserEmailContainingIgnoreCaseAndAddressContainingIgnoreCaseAndRoleId(
            Long userId, String fullName, String userEmail, String address, Integer roleId);

    /**
     * ✅ Find users by userId, full name, email, and role ID.
     */
    List<User> findByUserIdAndFullNameContainingIgnoreCaseAndUserEmailContainingIgnoreCaseAndRoleId(
            Long userId, String fullName, String userEmail, Integer roleId);

    /**
     * ✅ Find users by userId, full name, address, and role ID.
     */
    List<User> findByUserIdAndFullNameContainingIgnoreCaseAndAddressContainingIgnoreCaseAndRoleId(
            Long userId, String fullName, String address, Integer roleId);

    /**
     * ✅ Find users by userId, email, address, and role ID.
     */
    List<User> findByUserIdAndUserEmailContainingIgnoreCaseAndAddressContainingIgnoreCaseAndRoleId(
            Long userId, String userEmail, String address, Integer roleId);

    /**
     * ✅ Find users by full name and address.
     */
    List<User> findByFullNameContainingIgnoreCaseAndAddressContainingIgnoreCase(
            String fullName, String address);

    /**
     * ✅ Find users by email and address.
     */
    List<User> findByUserEmailContainingIgnoreCaseAndAddressContainingIgnoreCase(
            String userEmail, String address);

    /**
     * ✅ Find users by full name, email, address, and role ID.
     */
    List<User> findByFullNameContainingIgnoreCaseAndUserEmailContainingIgnoreCaseAndAddressContainingIgnoreCaseAndRoleId(
            String fullName, String userEmail, String address, Integer roleId);

    /**
     * ✅ Find users by userId, full name, email, and address.
     */
    List<User> findByUserIdAndFullNameContainingIgnoreCaseAndUserEmailContainingIgnoreCaseAndAddressContainingIgnoreCase(
            Long userId, String fullName, String userEmail, String address);

    // 🔹 Additional Query

    /**
     * ✅ Find a user by email (case-insensitive).
     */
    Optional<User> findByUserEmailIgnoreCase(String email);
}
