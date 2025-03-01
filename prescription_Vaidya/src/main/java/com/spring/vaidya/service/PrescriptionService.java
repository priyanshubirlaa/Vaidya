package com.spring.vaidya.service;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import com.spring.vaidya.entity.Prescription;

/**
 * 📌 Prescription Service Interface
 * 
 * This interface defines the contract for managing prescriptions.
 * It provides methods for CRUD operations and query functionalities.
 */
public interface PrescriptionService {

    /**
     * 📝 Creates a new prescription.
     * 
     * @param prescription The prescription entity to be saved.
     * @return The saved Prescription entity.
     * @throws FileNotFoundException If the prescription file is not found.
     */
    Prescription createPrescription(Prescription prescription) throws FileNotFoundException;

    /**
     * ✏️ Updates an existing prescription.
     * 
     * @param prescriptionId The ID of the prescription to be updated.
     * @param prescription The updated Prescription entity.
     * @return The updated Prescription entity.
     */
    Prescription updatePrescription(Long prescriptionId, Prescription prescription);

    /**
     * 🔍 Retrieves a prescription by its ID.
     * 
     * @param prescriptionId The ID of the prescription.
     * @return The Prescription entity if found.
     */
    Prescription getPrescriptionById(Long prescriptionId);

    /**
     * 🗑️ Deletes a prescription by its ID.
     * 
     * @param prescriptionId The ID of the prescription to be deleted.
     */
    void deletePrescription(Long prescriptionId);

    /**
     * 📜 Retrieves all prescriptions.
     * 
     * @return A list of all Prescription entities.
     */
    List<Prescription> getAllPrescriptions();

    /**
     * 🏥 Retrieves prescriptions for a specific user on a given date.
     * 
     * @param userId The ID of the user.
     * @param date The date for which prescriptions are needed.
     * @return A list of prescriptions matching the criteria.
     */
    List<Prescription> getPrescriptionsByUserIdAndDate(Long userId, LocalDate date);
}
