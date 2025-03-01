package com.spring.vaidya.controller;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.vaidya.entity.ErrorResponse;
import com.spring.vaidya.entity.Prescription;
import com.spring.vaidya.exception.FileStorageException;
import com.spring.vaidya.exception.PrescriptionNotFoundException;
import com.spring.vaidya.service.PrescriptionService;

/**
 * REST Controller for handling Prescription-related API endpoints.
 * Provides CRUD operations for prescriptions.
 */
@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "http://localhost:5173") // Allow frontend access from this origin
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    /**
     * ✅ Create a new prescription.
     * Handles potential file storage issues and other exceptions.
     *
     * @param prescription Prescription object to be saved.
     * @return ResponseEntity with created prescription or error response.
     */
    @PostMapping("/post")
    public ResponseEntity<?> createPrescription(@RequestBody Prescription prescription) {
        try {
            Prescription savedPrescription = prescriptionService.createPrescription(prescription);
            return new ResponseEntity<>(savedPrescription, HttpStatus.CREATED);
        } catch (FileNotFoundException e) {
            throw new FileStorageException("Prescription file not found: " + e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse("An error occurred while creating the prescription.", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * ✅ Update an existing prescription.
     *
     * @param id Prescription ID to update.
     * @param prescription Updated prescription details.
     * @return ResponseEntity with updated prescription or error response.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrescription(@PathVariable Long id, @RequestBody Prescription prescription) {
        try {
            Prescription updatedPrescription = prescriptionService.updatePrescription(id, prescription);
            return ResponseEntity.ok(updatedPrescription);
        } catch (PrescriptionNotFoundException e) {
            return buildErrorResponse(e.getMessage(), e, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return buildErrorResponse("Error updating prescription.", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * ✅ Retrieve a prescription by its ID.
     *
     * @param id Prescription ID.
     * @return ResponseEntity with prescription details or error response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPrescriptionById(@PathVariable Long id) {
        try {
            Prescription prescription = prescriptionService.getPrescriptionById(id);
            return ResponseEntity.ok(prescription);
        } catch (PrescriptionNotFoundException e) {
            return buildErrorResponse(e.getMessage(), e, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * ✅ Get prescriptions for a user on a specific date.
     *
     * @param userId User ID.
     * @param date Date (String format, parsed to LocalDate).
     * @return ResponseEntity with prescription list or error response.
     */
    @GetMapping("/user/{userId}/date/{date}")
    public ResponseEntity<?> getPrescriptionsByUserIdAndDate(
            @PathVariable Long userId,
            @PathVariable String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            List<Prescription> prescriptions = prescriptionService.getPrescriptionsByUserIdAndDate(userId, parsedDate);

            if (prescriptions.isEmpty()) {
                return buildErrorResponse("No prescriptions found for the given user and date.", null, HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            return buildErrorResponse("Invalid date format or request.", e, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * ✅ Delete a prescription by ID.
     *
     * @param id Prescription ID.
     * @return ResponseEntity confirming deletion or error response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrescription(@PathVariable Long id) {
        try {
            prescriptionService.deletePrescription(id);
            return ResponseEntity.ok("Prescription deleted successfully.");
        } catch (PrescriptionNotFoundException e) {
            return buildErrorResponse(e.getMessage(), e, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return buildErrorResponse("Error deleting prescription.", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * ✅ Retrieve all prescriptions.
     *
     * @return ResponseEntity with prescription list or error response.
     */
    @GetMapping
    public ResponseEntity<?> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
        if (prescriptions.isEmpty()) {
            return buildErrorResponse("No prescriptions found.", null, HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(prescriptions);
    }

    /**
     * 🔹 Helper method to build structured error responses.
     *
     * @param message Error message.
     * @param ex Exception (optional).
     * @param status HTTP status code.
     * @return ResponseEntity containing structured error response.
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, Exception ex, HttpStatus status) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        LocalDateTime.now(),  // Timestamp of the error
                        status.value(),       // HTTP status code
                        status.getReasonPhrase(), // HTTP status description
                        message + (ex != null ? " " + ex.getMessage() : "") // Full error message
                ),
                status
        );
    }
}
