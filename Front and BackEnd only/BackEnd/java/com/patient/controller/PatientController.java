package com.patient.controller;

import com.patient.model.Patient;
import com.patient.service.PatientService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("patient", new Patient());
        return "register";
    }

    
    
    @PostMapping("/register")
    public String registerPatient(@Valid @ModelAttribute Patient patient, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        
        try {
            patientService.savePatient(patient);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage()); // Add error message to the model
            return "register"; 
        }
        
        return "redirect:/login"; 
    }
    
    
    
    
    
    @PostMapping("/login")
    public String authenticatePatient(@RequestParam String email, @RequestParam String password, Model model) {
        Patient patient = patientService.findByEmail(email);
        
        
        if (patient != null && patient.getPassword().equals(password)) {
            model.addAttribute("patient", patient);
            return "redirect:/profile?email=" + email;
        }
        
        
        else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    @GetMapping("/profile")
    public String viewProfile(@RequestParam String email, Model model) {
        Patient patient = patientService.findByEmail(email);
        model.addAttribute("patient", patient);
        return "profile";
    }
    
    
    
    
    
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String verifyPatient(@RequestParam String email, @RequestParam String dateOfBirth, Model model) {
        Patient patient = patientService.findByEmail(email);
        if (patient != null && patient.getDateOfBirth().equals(LocalDate.parse(dateOfBirth))) {
            model.addAttribute("patient", patient);
            return "reset-password";
        } else {
            model.addAttribute("error", "Invalid email or date of birth");
            return "forgot-password";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email, @RequestParam String newPassword, Model model) {
        Patient patient = patientService.findByEmail(email);
        if (patient != null) {
            patient.setPassword(newPassword);
            patientService.savePatient(patient);
            return "redirect:/login";
        } else {
            model.addAttribute("error", "Invalid password");
            return "reset-password";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
