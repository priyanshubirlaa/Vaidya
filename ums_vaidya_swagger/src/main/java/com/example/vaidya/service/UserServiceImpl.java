package com.example.vaidya.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.vaidya.entity.User;
import com.example.vaidya.exception.UserNotFoundException;
import com.example.vaidya.repo.UserRepo;
import com.example.vaidya.specification.UserSpecification;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepo userRepository;

    @Override
    @Cacheable("users") 
    public List<User> getAllUsers() {
        logger.info("Fetching all users from the database");
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        logger.info("Fetching user with ID: {}", id);
        return Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", id);
                    return new UserNotFoundException("User not found with ID: " + id);
                }));
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        logger.info("Updating user with ID: {}", id);
        return userRepository.findById(id).map(user -> {
            user.setFullName(userDetails.getFullName());
            user.setUserEmail(userDetails.getUserEmail());
            user.setSpecialization(userDetails.getSpecialization());
            user.setQualification(userDetails.getQualification());
            user.setExperience(userDetails.getExperience());
            user.setAddress(userDetails.getAddress());
            user.setPassword(userDetails.getPassword());
            user.setEnabled(userDetails.isEnabled());
            user.setRoleId(userDetails.getRoleId());
            user.setAadharNo(userDetails.getAadharNo());
            logger.info("User with ID: {} updated successfully", id);
            return userRepository.save(user);
        }).orElseThrow(() -> {
            logger.error("User not found with ID: {}", id);
            return new UserNotFoundException("User not found with ID: " + id);
        });
    }

    @Override
    public void deleteUser(Long id) {
        logger.info("Attempting to delete user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            logger.error("User not found with ID: {}", id);
            throw new UserNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
        logger.info("User with ID: {} deleted successfully", id);
    }

    @Override
    public Page<User> getUsers(int page, int size) {
        logger.info("Fetching users with pagination - Page: {}, Size: {}", page, size);
        return userRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public List<User> filterUserss(Map<String, Object> filters) {
        logger.info("Filtering users based on criteria: {}", filters);
        Specification<User> specification = UserSpecification.getSpecification(filters);
        return userRepository.findAll(specification);
    }
    
    @Override
    public List<User> getAllUsersSorted(String sortBy, String sortDirection) {
        logger.info("Fetching sorted users by {} in {} order", sortBy, sortDirection);
        if (!List.of("fullName", "userEmail", "roleId", "gender").contains(sortBy)) {
            logger.error("Invalid sorting field: {}", sortBy);
            throw new IllegalArgumentException("Invalid sorting field: " + sortBy);
        }

        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        return userRepository.findAll(sort);
    }
}