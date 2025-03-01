package com.spring.vaidya.repo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.vaidya.entity.Slot;
import com.spring.vaidya.entity.User;

/**
 * Repository interface for managing Slot entities.
 * Extends JpaRepository to provide CRUD operations.
 */
public interface SlotRepository extends JpaRepository<Slot, Long> {
    
    /**
     * Retrieves a list of slots available for a given date.
     *
     * @param date The date for which slots need to be fetched.
     * @return List of slots for the specified date.
     */
    List<Slot> findByDate(LocalDate date);

    /**
     * Checks if a slot exists for a doctor on a given date where the slot overlaps 
     * with an existing booking.
     *
     * @param doctor      The doctor for whom the slot is being checked.
     * @param date        The date of the slot.
     * @param slotEndTime The end time of the new slot.
     * @param currentTime The start time of the existing slot.
     * @return true if there is an overlapping slot, false otherwise.
     */
    boolean existsByDoctorAndDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            User doctor, LocalDate date, LocalTime slotEndTime, LocalTime currentTime);

    /**
     * Retrieves slots for a specific doctor on a given date.
     *
     * @param date   The date of the slots.
     * @param userId The doctor's user ID.
     * @return List of slots for the given doctor and date.
     */
    List<Slot> findByDateAndDoctor_UserId(LocalDate date, Long userId);
}
