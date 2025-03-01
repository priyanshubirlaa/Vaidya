package com.spring.vaidya.serviceImpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.vaidya.entity.Patient;
import com.spring.vaidya.entity.Prescription;
import com.spring.vaidya.entity.Slot;
import com.spring.vaidya.entity.User;
import com.spring.vaidya.exception.DuplicatePrescriptionException;
import com.spring.vaidya.exception.InvalidPrescriptionException;
import com.spring.vaidya.exception.PatientNotFoundException;
import com.spring.vaidya.exception.PrescriptionNotFoundException;
import com.spring.vaidya.exception.SlotNotFoundException;
import com.spring.vaidya.exception.UserNotFoundException;
import com.spring.vaidya.repo.PatientRepository;
import com.spring.vaidya.repo.PrescriptionRepository;
import com.spring.vaidya.repo.SlotRepository;
import com.spring.vaidya.repo.UserRepository;
import com.spring.vaidya.service.PrescriptionService;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private PatientRepository patientRepository;

    // ✅ Get Prescriptions by User ID and Date
    @Override
    public List<Prescription> getPrescriptionsByUserIdAndDate(Long userId, LocalDate date) {
        return prescriptionRepository.findByUser_UserIdAndDate(userId, date);
    }

    // ✅ Update Prescription with Proper Exception Handling
    @Override
    public Prescription updatePrescription(Long prescriptionId, Prescription prescriptionDetails) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new PrescriptionNotFoundException(prescriptionId));

        prescription.setFever(prescriptionDetails.getFever());
        prescription.setWeight(prescriptionDetails.getWeight());
        prescription.setBp(prescriptionDetails.getBp());
        prescription.setSugar(prescriptionDetails.getSugar());
        prescription.setTest(prescriptionDetails.getTest());
        prescription.setMedicine(prescriptionDetails.getMedicine());
        prescription.setHistory(prescriptionDetails.getHistory());

        prescription.setUser(getUserById(prescriptionDetails.getUser().getUserId()));
        prescription.setSlot(getSlotById(prescriptionDetails.getSlot().getSlotId()));
        prescription.setPatient(getPatientById(prescriptionDetails.getPatient().getPatientId()));

        return prescriptionRepository.save(prescription);
    }

    // ✅ Get Prescription by ID
    @Override
    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException(id));
    }

    // ✅ Delete Prescription
    @Override
    public void deletePrescription(Long prescriptionId) {
        if (!prescriptionRepository.existsById(prescriptionId)) {
            throw new PrescriptionNotFoundException(prescriptionId);
        }
        prescriptionRepository.deleteById(prescriptionId);
    }

    // ✅ Get All Prescriptions
    @Override
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    // ✅ Create Prescription with Proper Validation
    @Override
    public Prescription createPrescription(Prescription prescription) {
        if (prescription == null) {
            throw new InvalidPrescriptionException("Prescription data is missing.");
        }

        // Validate and fetch related User, Slot, and Patient
        User user = getUserById(prescription.getUser().getUserId());
        Slot slot = getSlotById(prescription.getSlot().getSlotId());
        Patient patient = getPatientById(prescription.getPatient().getPatientId());

        // Check if a prescription already exists for the same slot ID
        boolean exists = prescriptionRepository.existsBySlot_SlotId(slot.getSlotId());
        if (exists) {
            throw new DuplicatePrescriptionException("A prescription already exists for slot ID: " + slot.getSlotId());
        }

        // Set validated entities to the prescription
        prescription.setUser(user);
        prescription.setSlot(slot);
        prescription.setPatient(patient);

        return prescriptionRepository.save(prescription);
    }



    // 🔹 Helper Methods for Fetching Related Entities with Exception Handling
    private User getUserById(Long userId) {
        return userRepository.findById(userId) // ✅ Correct: Lowercase "u"
            .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));
    }


    private Slot getSlotById(Long slotId) {
        return slotRepository.findById(slotId)
                .orElseThrow(() -> new SlotNotFoundException(slotId));
    }

    private Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
    }
}
