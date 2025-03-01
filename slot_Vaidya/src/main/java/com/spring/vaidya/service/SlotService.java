package com.spring.vaidya.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.vaidya.entity.Slot;
import com.spring.vaidya.entity.User;
import com.spring.vaidya.exception.DoctorNotFoundException;
import com.spring.vaidya.exception.SlotNotFoundException;
import com.spring.vaidya.exception.SlotOverlapException;
import com.spring.vaidya.repo.SlotRepository;
import com.spring.vaidya.repo.UserRepository;

/**
 * Service class for managing slot-related operations.
 */
@Service
public class SlotService {

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private UserRepository doctorRepository;

    /**
     * Creates multiple time slots for a doctor within the specified time range.
     * Ensures that no overlapping slots exist on the same date.
     *
     * @param startTime The start time of the slots.
     * @param endTime   The end time of the slots.
     * @param slotRange The duration of each slot (e.g., "10 minutes").
     * @param userId    The ID of the doctor creating the slots.
     * @param date      The date for which the slots are created.
     * @return A list of created Slot objects.
     */
    public List<Slot> createSlots(LocalTime startTime, LocalTime endTime, String slotRange, Long userId, LocalDate date) {
        // Retrieve the doctor by ID, throw exception if not found
        User doctor = doctorRepository.findById(userId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with ID: " + userId));

        // Parse slot duration from the slotRange string (e.g., "10 minutes" → 10)
        int slotMinutes;
        try {
            slotMinutes = Integer.parseInt(slotRange.split(" ")[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid slot range format: " + slotRange);
        }

        List<Slot> createdSlots = new ArrayList<>();
        LocalTime currentTime = startTime;

        while (!currentTime.plus(slotMinutes - 1, ChronoUnit.MINUTES).isAfter(endTime)) {
            LocalTime slotEndTime = currentTime.plus(slotMinutes - 1, ChronoUnit.MINUTES);

            // Check if a slot overlaps with an existing one
            boolean overlappingSlotExists = slotRepository.existsByDoctorAndDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                    doctor, date, slotEndTime, currentTime);

            if (overlappingSlotExists) {
                throw new SlotOverlapException("Cannot create overlapping slot for doctor ID: " + userId + " on " + date);
            }

            // Create new slot
            Slot slot = new Slot();
            slot.setStartTime(currentTime);
            slot.setEndTime(slotEndTime);
            slot.setSlotRange(slotRange);
            slot.setDoctor(doctor);
            slot.setDate(date);
            slot.setStatus("yes"); // Default to available

            createdSlots.add(slotRepository.save(slot));

            // Move to the next slot start time
            currentTime = slotEndTime.plus(1, ChronoUnit.MINUTES);
        }

        return createdSlots;
    }

    /**
     * Updates the status of a slot (e.g., booked, available, cancelled).
     *
     * @param slotId The ID of the slot to update.
     * @param status The new status of the slot.
     * @return The updated Slot object.
     */
    public Slot updateSlotStatus(Long slotId, String status) {
        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new SlotNotFoundException("Slot not found with ID: " + slotId));

        slot.setStatus(status);
        return slotRepository.save(slot);
    }

    /**
     * Checks whether a slot is available.
     *
     * @param slotId The ID of the slot.
     * @return true if the slot is available, false otherwise.
     */
    public boolean isSlotAvailable(Long slotId) {
        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new SlotNotFoundException("Slot not found with ID: " + slotId));

        return "yes".equals(slot.getStatus());
    }

    /**
     * Retrieves all slots for a given date.
     *
     * @param date The date to search for available slots.
     * @return A list of Slot objects.
     */
    public List<Slot> getSlotsByDate(LocalDate date) {
        return slotRepository.findByDate(date);
    }

    /**
     * Retrieves all slots for a given date and doctor.
     *
     * @param date   The date to search for available slots.
     * @param userId The ID of the doctor.
     * @return A list of Slot objects.
     */
    public List<Slot> getSlotsByDateAndUserId(LocalDate date, Long userId) {
        return slotRepository.findByDateAndDoctor_UserId(date, userId);
    }
}
