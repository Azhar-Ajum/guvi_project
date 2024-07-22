package com.patient.service;

import com.patient.model.Patient;
import com.patient.repository.PatientRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @InjectMocks
    private PatientService patientService;

    @Mock
    private PatientRepository patientRepository;

    @Test
    public void testSavePatient() {
        // Test success case
        Patient patient1 = new Patient();
        patient1.setEmail("test@example.com");
        when(patientRepository.save(any(Patient.class))).thenReturn(patient1);
        Patient savedPatient = patientService.savePatient(patient1);
        assertNotNull(savedPatient);
        assertEquals("test@example.com", savedPatient.getEmail());

        // Test email already exists case
        Patient patient2 = new Patient();
        patient2.setEmail("existing@example.com");
        when(patientRepository.save(any(Patient.class)))
                .thenThrow(new DataIntegrityViolationException("Email already exists", 
                        new org.hibernate.exception.ConstraintViolationException("Duplicate entry", null, "email")));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            patientService.savePatient(patient2);
        });
        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    public void testFindByEmail_Success() {
        Patient patient = new Patient();
        patient.setEmail("test@example.com");

        when(patientRepository.findByEmail("test@example.com")).thenReturn(Optional.of(patient));

        Patient foundPatient = patientService.findByEmail("test@example.com");

        assertNotNull(foundPatient);
        assertEquals("test@example.com", foundPatient.getEmail());
    }

    @Test
    public void testFindByEmail_NotFound() {
        when(patientRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Patient foundPatient = patientService.findByEmail("nonexistent@example.com");

        assertNull(foundPatient);
    }
}
