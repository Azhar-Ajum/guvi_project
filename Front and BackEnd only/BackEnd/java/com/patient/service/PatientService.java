package com.patient.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.patient.model.Patient;
import com.patient.repository.PatientRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    
    
    public Patient savePatient(Patient patient) {
        try {
            return patientRepository.save(patient);
        } catch (DataIntegrityViolationException e) {
            // Check if the exception is caused by email or password constraint violation
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                org.hibernate.exception.ConstraintViolationException constraintViolationException =
                        (org.hibernate.exception.ConstraintViolationException) e.getCause();
                if (constraintViolationException.getConstraintName().contains("email")) {
                    throw new RuntimeException("Email already exists");
                }
            }
            throw new RuntimeException("This Mail Already EXists");
        }
    }
    
    
     public Patient findByEmail(String email) {
        return patientRepository.findByEmail(email).orElse(null);
    }
}
