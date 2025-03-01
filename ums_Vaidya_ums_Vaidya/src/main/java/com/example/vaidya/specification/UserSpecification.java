package com.example.vaidya.specification;

import org.springframework.data.jpa.domain.Specification;
import com.example.vaidya.entity.User;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;

/**
 * 📌 UserSpecification - Dynamic Query Builder for Filtering Users
 * - Uses `Specification` to create dynamic query predicates based on input filters.
 * - Supports filtering by full name, email, and gender.
 */
public class UserSpecification {
   
    /**
     * ✅ Generates a dynamic Specification based on provided filters.
     * @param filters A map of field names and their corresponding filter values.
     * @return A Specification<User> instance with applied filters.
     */
    public static Specification<User> getSpecification(Map<String, Object> filters) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();  // Default to true (AND condition)

            // 🔹 Filter by fullName (case-insensitive search)
            if (filters.containsKey("fullName")) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), 
                    "%" + filters.get("fullName").toString().toLowerCase() + "%"));
            }

            // 🔹 Filter by userEmail (case-insensitive search)
            if (filters.containsKey("userEmail")) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("userEmail")), 
                    "%" + filters.get("userEmail").toString().toLowerCase() + "%"));
            }

            // 🔹 Filter by gender (Exact match)
            if (filters.containsKey("gender")) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.equal(root.get("gender"), filters.get("gender")));
            }
            
         // Filter for roleId
	        if (filters.containsKey("roleId")) {
	            predicate = criteriaBuilder.and(predicate, 
	                criteriaBuilder.equal(root.get("roleId"), filters.get("roleId")));
	        }

            return predicate;
        };
    }
}
