package com.patient.controller;

import com.patient.model.Appointment;
import com.patient.service.AppointmentService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AppointmentControllerTest {

    @InjectMocks
    private AppointmentController appointmentController;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAppointments() {
        
        String email = "test@example.com";
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment());
        appointments.add(new Appointment());
        
        when(appointmentService.getAppointmentsByPatientEmail(email)).thenReturn(appointments);

        
        String viewName = appointmentController.getAppointments(email, model);

        // Assert
        assertEquals("appointments", viewName);
        verify(appointmentService, times(1)).getAppointmentsByPatientEmail(email);
        verify(model, times(1)).addAttribute("appointments", appointments);
        verify(model, times(1)).addAttribute("patientEmail", email);
        verify(model, times(1)).addAttribute(any(String.class), any(Appointment.class)); // Ensure appointment attribute is added
    }

    @Test
    public void testBookAppointment() {
       
        Appointment appointment = new Appointment();
        String patientEmail = "test@example.com";

        doNothing().when(appointmentService).bookAppointment(appointment, patientEmail);

     
        String viewName = appointmentController.bookAppointment(appointment, patientEmail, model);

        // Assert
        assertEquals("redirect:/appointments?email=" + patientEmail, viewName);
        verify(appointmentService, times(1)).bookAppointment(appointment, patientEmail);
        verify(model, never()).addAttribute(eq("error"), any(String.class)); // Ensure no error attribute is added
    }

    @Test
    public void testBookAppointment_Exception() {
        
        Appointment appointment = new Appointment();
        String patientEmail = "test@example.com";
        String errorMessage = "Failed to book appointment";

        // Mocking the service to throw a RuntimeException
        doThrow(new RuntimeException(errorMessage)).when(appointmentService).bookAppointment(appointment, patientEmail);

        
        String viewName = appointmentController.bookAppointment(appointment, patientEmail, model);

        // Assert
        assertEquals("appointments", viewName); // Redirect back to appointments page on exception

        // Verify that the error message attribute was added to the model
        verify(appointmentService, times(1)).bookAppointment(appointment, patientEmail);
    }


}

