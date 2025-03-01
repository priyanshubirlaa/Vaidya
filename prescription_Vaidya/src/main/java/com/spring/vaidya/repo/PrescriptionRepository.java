package com.spring.vaidya.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spring.vaidya.entity.Prescription;

/**
 * 🗄️ Prescription Repository Interface
 * 
 * This interface extends JpaRepository, providing built-in CRUD operations 
 * for managing Prescription entities.
 */
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    /**
     * 🔍 Fetches prescriptions by user ID and date.
     * 
     * @param userId The ID of the user.
     * @param date The specific date for which prescriptions are needed.
     * @return A list of prescriptions belonging to the user on the given date.
     */
    List<Prescription> findByUser_UserIdAndDate(Long userId, LocalDate date);

    /**
     * 🏥 Checks if a prescription exists for a specific appointment slot.
     * 
     * @param slotId The ID of the slot to check.
     * @return True if a prescription exists for the given slot, false otherwise.
     */
    boolean existsBySlot_SlotId(Long slotId);
}
