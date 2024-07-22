package com.patient.controller;

import com.patient.model.Appointment;
import com.patient.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public String getAppointments(@RequestParam String email, Model model) {
        List<Appointment> appointments = appointmentService.getAppointmentsByPatientEmail(email);
        model.addAttribute("appointments", appointments);
        model.addAttribute("patientEmail", email);
        model.addAttribute("appointment", new Appointment());
        return "appointments";
    }
    
    
    
    
    @PostMapping("/book")
    public String bookAppointment(@ModelAttribute Appointment appointment, @RequestParam String patientEmail, Model model) {
        
    	try {
            appointmentService.bookAppointment(appointment, patientEmail);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to book appointment: " + e.getMessage());
            
            return "appointments"; // Return to appointments page with error message
        }

        return "redirect:/appointments?email=" + patientEmail;
    }

}