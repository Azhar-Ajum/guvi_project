package com.patient.service;

import com.patient.model.Medication;
import com.patient.repository.MedicationRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicationServiceTest {

    @InjectMocks
    private MedicationService medicationService;

    @Mock
    private MedicationRepository medicationRepository;

    @Test
    public void testGetAllMedications() {
        
        List<Medication> medications = List.of(new Medication(), new Medication());
        when(medicationRepository.findAll()).thenReturn(medications);

        
        List<Medication> result = medicationService.getAllMedications();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(medicationRepository, times(1)).findAll();
    }

    @Test
    public void testGetMedicationsByPatientId() {
       
        Long patientId = 1L;
        List<Medication> medications = List.of(new Medication(), new Medication());
        when(medicationRepository.findByPatientId(patientId)).thenReturn(medications);

     
        List<Medication> result = medicationService.getMedicationsByPatientId(patientId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(medicationRepository, times(1)).findByPatientId(patientId);
    }

    @Test
    public void testGetMedicationById() {
        // Arrange
        Long id = 1L;
        Medication medication = new Medication();
        when(medicationRepository.findById(id)).thenReturn(Optional.of(medication));

        // Act
        Optional<Medication> result = medicationService.getMedicationById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(medication, result.get());
        verify(medicationRepository, times(1)).findById(id);
    }

    @Test
    public void testSaveMedication() {
        // Arrange
        Medication medication = new Medication();
        when(medicationRepository.save(any(Medication.class))).thenReturn(medication);

        // Act
        Medication result = medicationService.saveMedication(medication);

        // Assert
        assertNotNull(result);
        assertEquals(medication, result);
        verify(medicationRepository, times(1)).save(medication);
    }

    @Test
    public void testDeleteMedication() {
        // Arrange
        Long id = 1L;
        doNothing().when(medicationRepository).deleteById(id);

        // Act
        medicationService.deleteMedication(id);

        // Assert
        verify(medicationRepository, times(1)).deleteById(id);
    }
}
