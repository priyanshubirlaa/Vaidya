package com.spring.vaidya.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.vaidya.entity.Patient;
import com.spring.vaidya.entity.ErrorResponse;
import com.spring.vaidya.service.PatientService;

/**
 * Controller for managing patient-related operations.
 */
@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:5173")
public class PatientController {

    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);
    private final PatientService patientService;

    /**
     * Constructor for injecting dependencies.
     * 
     * @param patientService Service layer for patient operations.
     */
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Creates a new patient.
     * 
     * @param patient Patient object to be created.
     * @return ResponseEntity containing the created patient or error response.
     */
    @PostMapping("/post")
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
        try {
            Patient savedPatient = patientService.savePatient(patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
        } catch (Exception e) {
            logger.error("Error creating patient: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                                            "Failed to create patient", e.getMessage()));
        }
    }

    /**
     * Retrieves all patients.
     * 
     * @return List of all patients.
     */
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    /**
     * Retrieves a patient by their ID.
     * 
     * @param id Patient ID.
     * @return ResponseEntity containing the patient or error response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        try {
            Patient patient = patientService.getPatientById(id);
            return ResponseEntity.ok(patient);
        } catch (Exception e) {
            logger.error("Patient with ID {} not found: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), 
                                            "Patient not found", e.getMessage()));
        }
    }

    /**
     * Updates an existing patient.
     * 
     * @param id Patient ID.
     * @param patient Updated patient details.
     * @return ResponseEntity containing updated patient or error response.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        try {
            Patient updatedPatient = patientService.updatePatient(id, patient);
            return ResponseEntity.ok(updatedPatient);
        } catch (Exception e) {
            logger.error("Error updating patient with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), 
                                            "Failed to update patient", e.getMessage()));
        }
    }

    /**
     * Deletes a patient by ID.
     * 
     * @param id Patient ID.
     * @return ResponseEntity indicating success or failure.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting patient with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                                            "Failed to delete patient", e.getMessage()));
        }
    }

    /**
     * Retrieves patients by their mobile number.
     * 
     * @param phoneNumber Mobile number of the patient(s).
     * @return ResponseEntity containing a list of patients or error response.
     */
    @GetMapping("/search1")
    public ResponseEntity<?> getPatientsByphoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        List<Patient> patients = patientService.getPatientsByphoneNumber(phoneNumber);
        if (patients.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), 
                                            "No patients found with this mobile number", ""));
        }
        return ResponseEntity.ok(patients);
    }

    /**
     * Retrieves a patient by their slot ID.
     * 
     * @param slotId Slot ID associated with the patient.
     * @return ResponseEntity containing the patient or not found status.
     */
    @GetMapping("/slot/{slotId}")
    public ResponseEntity<Patient> getPatientBySlotId(@PathVariable Long slotId) {
        Optional<Patient> patient = patientService.getPatientBySlotId(slotId);
        return patient.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves patients by the doctor’s user ID.
     * 
     * @param userId Doctor's user ID.
     * @return ResponseEntity containing a list of patients or error response.
     */
    @GetMapping("/doctor/{userId}")
    public ResponseEntity<?> getPatientsByDoctorUserId(@PathVariable Long userId) {
        List<Patient> patients = patientService.getPatientsByDoctorUserId(userId);
        if (patients.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), 
                                            "No patients found for this doctor", ""));
        }
        return ResponseEntity.ok(patients);
    }

    /**
     * Retrieves patients by the doctor’s user ID and appointment date.
     * 
     * @param userId Doctor's user ID.
     * @param date Appointment date in ISO format (YYYY-MM-DD).
     * @return ResponseEntity containing a list of patients or error response.
     */
    @GetMapping("/doctor/{userId}/date/{date}")
    public ResponseEntity<?> getPatientsByDoctorUserIdAndDate(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date.trim());
            List<Patient> patients = patientService.getPatientsByDoctorUserIdAndDate(userId, parsedDate);

            if (patients.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), 
                                                "No patients found for this doctor on this date", ""));
            }
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            logger.error("Error parsing date {}: {}", date, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), 
                                            "Invalid date format", e.getMessage()));
        }
    }
}
