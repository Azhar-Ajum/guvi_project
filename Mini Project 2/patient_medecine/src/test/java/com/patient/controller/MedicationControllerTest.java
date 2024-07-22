package com.patient.controller;

import com.patient.model.Medication;
import com.patient.model.Patient;
import com.patient.service.MedicationService;
import com.patient.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicationControllerTest {

    @InjectMocks
    private MedicationController medicationController;

    @Mock
    private MedicationService medicationService;

    @Mock
    private PatientService patientService;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testViewMedications() {
        
        List<Medication> medications = new ArrayList<>();
        when(medicationService.getAllMedications()).thenReturn(medications);

       
        String viewName = medicationController.viewMedications(model);

        // Assert
        assertEquals("medications", viewName);
        verify(medicationService, times(1)).getAllMedications();
        verify(model, times(1)).addAttribute("medications", medications);
    }

    @Test
    public void testSaveMedication() {
        
        String email = "test@example.com";
        Patient patient = new Patient();
        patient.setEmail(email);
        Medication medication = new Medication();
        medication.setId(1L);

        when(patientService.findByEmail(email)).thenReturn(patient);

       
        String redirectView = medicationController.saveMedication(email, medication);

        // Assert
        assertEquals("redirect:/patient-medications?email=" + email, redirectView);
        assertEquals(patient, medication.getPatient());
        verify(patientService, times(1)).findByEmail(email);
        verify(medicationService, times(1)).saveMedication(any(Medication.class));
    }

    @Test
    public void testUpdateMedicationForm() {
        
        Long id = 1L;
        String email = "test@example.com";
        Optional<Medication> medicationOptional = Optional.of(new Medication());

        when(medicationService.getMedicationById(id)).thenReturn(medicationOptional);

      
        String viewName = medicationController.updateMedicationForm(id, email, model);

        // Assert
        assertEquals("update_medication", viewName);
        verify(medicationService, times(1)).getMedicationById(id);
        verify(model, times(1)).addAttribute("medication", medicationOptional.get());
        verify(model, times(1)).addAttribute("email", email);
    }


    @Test
    public void testUpdateMedication() {
        
        Long id = 1L;
        String email = "test@example.com";
        Medication medication = new Medication();
        medication.setId(id);

        Patient patient = new Patient();
        patient.setEmail(email);

        when(patientService.findByEmail(email)).thenReturn(patient);

        
        String redirectView = medicationController.updateMedication(id, email, medication);

        // Assert
        assertEquals("redirect:/patient-medications?email=" + email, redirectView);
        assertEquals(id, medication.getId());
        verify(patientService, times(1)).findByEmail(email);
        verify(medicationService, times(1)).saveMedication(any(Medication.class));
    }

    @Test
    public void testDeleteMedication() {
        
        Long id = 1L;
        String email = "test@example.com";
        doNothing().when(medicationService).deleteMedication(id);

        
        String redirectView = medicationController.deleteMedication(id, email);

        // Assert
        assertEquals("redirect:/patient-medications?email=" + email, redirectView);
        verify(medicationService, times(1)).deleteMedication(id);
    }

    @Test
    public void testViewMedicationsByPatientEmail() {
        
        String email = "test@example.com";
        Patient patient = new Patient();
        patient.setEmail(email);
        List<Medication> medications = new ArrayList<>();
        when(patientService.findByEmail(email)).thenReturn(patient);
        when(medicationService.getMedicationsByPatientId(patient.getId())).thenReturn(medications);

        
        String viewName = medicationController.viewMedicationsByPatientEmail(email, model);

        // Assert
        assertEquals("medications", viewName);
        verify(patientService, times(1)).findByEmail(email);
        verify(medicationService, times(1)).getMedicationsByPatientId(patient.getId());
        verify(model, times(1)).addAttribute("patient", patient);
        verify(model, times(1)).addAttribute("medications", medications);
    }
}
