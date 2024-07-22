package com.patient.controller;

import com.patient.model.Medication;
import com.patient.model.Patient;
import com.patient.service.MedicationService;
import com.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class MedicationController {

    @Autowired
    private MedicationService medicationService;

    @Autowired
    private PatientService patientService;

    // Display list of medications
    @GetMapping("/medications")
    public String viewMedications(Model model) {
        List<Medication> medications = medicationService.getAllMedications();
        model.addAttribute("medications", medications);
        return "medications";
    }

    @PostMapping("/saveMedication")
    public String saveMedication(@RequestParam String email, @ModelAttribute Medication medication) {
        Patient patient = patientService.findByEmail(email);
        medication.setPatient(patient);
        medicationService.saveMedication(medication);
        return "redirect:/patient-medications?email=" + email;
    }




        // Update medication (get form)
        @GetMapping("/updateMedication/{id}")
        public String updateMedicationForm(@PathVariable Long id, @RequestParam String email, Model model) {
            Optional<Medication> medication = medicationService.getMedicationById(id);
            model.addAttribute("medication", medication.orElse(null));
            model.addAttribute("email", email); // Add email for reference
            return "update_medication";
        }

        // Update medication (post form)
        @PostMapping("/updateMedication/{id}")
        public String updateMedication(@PathVariable Long id, @RequestParam String email, @ModelAttribute Medication medication) {
            Patient patient = patientService.findByEmail(email);
            medication.setId(id);
            medication.setPatient(patient);
            medicationService.saveMedication(medication);
            return "redirect:/patient-medications?email=" + email;
        }
    


    // Delete medication
    @PostMapping("/deleteMedication/{id}")
    public String deleteMedication(@PathVariable Long id, @RequestParam String email) {
        medicationService.deleteMedication(id);
        return "redirect:/patient-medications?email=" + email;
    }

    // Get medications by patient email
    @GetMapping("/patient-medications")
    public String viewMedicationsByPatientEmail(@RequestParam String email, Model model) {
        Patient patient = patientService.findByEmail(email);
        List<Medication> medications = medicationService.getMedicationsByPatientId(patient.getId());
        model.addAttribute("patient", patient);
        model.addAttribute("medications", medications);
        return "medications";
    }
}
