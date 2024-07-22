package com.patient.repository;

import com.patient.model.Medication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicationRepositoryTest {

    @Mock
    private MedicationRepository medicationRepository; // Mocking the interface

    @Test
    public void testFindByPatientId() {
        
        Long patientId = 1L;
        Medication medication1 = new Medication();
        Medication medication2 = new Medication();
        List<Medication> medications = new ArrayList<>();
        medications.add(medication1);
        medications.add(medication2);

        when(medicationRepository.findByPatientId(patientId)).thenReturn(medications);

        
        List<Medication> foundMedications = medicationRepository.findByPatientId(patientId);

        // Assert
        assertEquals(2, foundMedications.size());
    }
}
