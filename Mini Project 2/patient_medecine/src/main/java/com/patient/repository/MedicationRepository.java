package com.patient.repository;

import com.patient.model.Medication;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
	
	 List<Medication> findByPatientId(Long patientId);
   
}

