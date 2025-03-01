package com.spring.vaidya.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.vaidya.entity.Slot;
import com.spring.vaidya.entity.SlotRequest;
import com.spring.vaidya.exception.SlotNotFoundException;
import com.spring.vaidya.service.SlotService;

/**
 * Controller for managing slot-related operations.
 */
@RestController
@RequestMapping("/api/slots")
@CrossOrigin(origins = "http://localhost:5173") // Allows frontend requests from this origin
public class SlotController {

    @Autowired
    private SlotService slotService;

    /**
     * Creates slots for a given time range, slot duration, user ID, and date.
     *
     * @param slotRequest The request object containing slot creation details.
     * @return List of created slots wrapped in a ResponseEntity.
     */
    @PostMapping("/create")
    public ResponseEntity<List<Slot>> createSlots(@RequestBody SlotRequest slotRequest) {
        LocalTime startTime = slotRequest.getStartTime();
        LocalTime endTime = slotRequest.getEndTime();
        String slotRange = slotRequest.getSlotRange();
        Long userId = slotRequest.getUserId();
        LocalDate date = slotRequest.getDate();

        List<Slot> slots = slotService.createSlots(startTime, endTime, slotRange, userId, date);
        return ResponseEntity.ok(slots);
    }

    /**
     * Updates the status of a slot (e.g., available, booked).
     *
     * @param slotId The ID of the slot to update.
     * @param status The new status of the slot.
     * @return Updated slot details if successful; otherwise, an error message.
     */
    @PutMapping("/{slotId}")
    public ResponseEntity<?> updateSlotStatus(@PathVariable Long slotId, @RequestParam String status) {
        try {
            Slot slot = slotService.updateSlotStatus(slotId, status);
            return ResponseEntity.ok(slot);
        } catch (SlotNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    /**
     * Checks if a specific slot is available.
     *
     * @param slotId The ID of the slot to check.
     * @return True if the slot is available; false otherwise.
     */
    @GetMapping("/{slotId}/availability")
    public ResponseEntity<Boolean> isSlotAvailable(@PathVariable Long slotId) {
        boolean available = slotService.isSlotAvailable(slotId);
        return ResponseEntity.ok(available);
    }

    /**
     * Retrieves all slots available on a specific date.
     *
     * @param date The date for which to fetch slots.
     * @return List of slots available on the specified date, or an error message if no slots are found.
     */
    @GetMapping("/by-date")
    public ResponseEntity<?> getSlotsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Slot> slots = slotService.getSlotsByDate(date);
        if (slots.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No slots found for the given date.");
        }
        return ResponseEntity.ok(slots);
    }

    /**
     * Retrieves slots for a specific user (doctor) on a given date.
     *
     * @param date   The date for which to fetch slots.
     * @param userId The ID of the doctor (user).
     * @return List of slots matching the date and user ID, or an error message if no slots are found.
     */
    @GetMapping("/search")
    public ResponseEntity<?> getSlotsByDateAndUserId(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("userId") Long userId) {
        List<Slot> slots = slotService.getSlotsByDateAndUserId(date, userId);
        if (slots.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No slots found for the given date and user.");
        }
        return ResponseEntity.ok(slots);
    }
}
