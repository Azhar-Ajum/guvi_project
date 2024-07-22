package com.patient.repository;

import com.patient.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientRepositoryTest {

    @Mock
    private PatientRepository patientRepository; // Mocking the interface

    @Test
    public void testFindByEmail() {
        
        String email = "test@example.com";
        Patient patient = new Patient();
        patient.setEmail(email);
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));

        
        Optional<Patient> foundPatient = patientRepository.findByEmail(email);

        // Assert
        assertEquals(email, foundPatient.orElseThrow().getEmail());
    }
}
