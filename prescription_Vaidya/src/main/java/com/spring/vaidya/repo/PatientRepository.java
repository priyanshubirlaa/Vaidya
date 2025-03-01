package com.spring.vaidya.repo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.vaidya.entity.Patient;
import com.spring.vaidya.entity.Prescription;


public interface PatientRepository extends JpaRepository<Patient, Long>{

	
}
