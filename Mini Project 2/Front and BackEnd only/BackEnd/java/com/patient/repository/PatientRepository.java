package com.patient.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.patient.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
   Optional <Patient> findByEmail(String email);

}

