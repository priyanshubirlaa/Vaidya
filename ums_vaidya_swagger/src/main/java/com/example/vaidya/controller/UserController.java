package com.example.vaidya.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.vaidya.entity.ErrorResponse;
import com.example.vaidya.entity.User;
import com.example.vaidya.exception.UserNotFoundException;
import com.example.vaidya.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://127.0.0.1:5500")
@Tag(name = "User Controller", description = "Handles all user-related operations.")
@OpenAPIDefinition(info = @Info(title = "User API", version = "1.0", description = "API for managing users."))
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Fetch all users", description = "Retrieves all users from the database.")
    public ResponseEntity<?> getAllUsers() {
        logger.info("Fetching all users");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            logger.warn("No users found");
            return buildErrorResponse("No users found.", HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Fetches user details based on the provided ID.")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        logger.info("Fetching user with ID: {}", id);
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            logger.error("User not found with ID: {}", id);
            return buildErrorResponse("User not found with ID: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates user details based on the given ID.")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        logger.info("Updating user with ID: {}", id);
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error updating user: {}", e.getMessage());
            return buildErrorResponse("Error updating user.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user by ID.")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (UserNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error deleting user: {}", e.getMessage());
            return buildErrorResponse("Error deleting user.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pagination")
    @Operation(summary = "Get paginated users", description = "Fetches users in paginated format.")
    public ResponseEntity<?> getUsers(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        logger.info("Fetching users with pagination: page={}, size={}", page, size);
        Page<User> userPage = userService.getUsers(page, size);
        if (userPage.isEmpty()) {
            logger.warn("No users found for the given page.");
            return buildErrorResponse("No users found for the given page.", HttpStatus.NO_CONTENT);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("users", userPage.getContent());
        response.put("totalPages", userPage.getTotalPages());
        response.put("currentPage", userPage.getNumber());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter users", description = "Filters users based on provided criteria.")
    public ResponseEntity<?> filterUsers(@RequestParam Map<String, Object> filters) {
        logger.info("Filtering users with criteria: {}", filters);
        try {
            List<User> users = userService.filterUserss(filters);
            if (users.isEmpty()) {
                logger.warn("No users found matching the filters.");
                return buildErrorResponse("No users found matching the filters.", HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Error filtering users: {}", e.getMessage());
            return buildErrorResponse("Error filtering users.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sorted")
    @Operation(summary = "Get sorted users", description = "Retrieves users sorted by a specific field.")
    public List<User> getSortedUsers(@RequestParam(defaultValue = "fullName") String sortBy,
                                     @RequestParam(defaultValue = "asc") String sortDirection) {
        logger.info("Fetching sorted users by {} in {} order", sortBy, sortDirection);
        return userService.getAllUsersSorted(sortBy, sortDirection);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
        logger.error("Error Response: {}, Status: {}", message, status);
        return new ResponseEntity<>(
                new ErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message),
                status);
    }
}