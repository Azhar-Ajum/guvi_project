package com.patient.service;

import com.patient.model.Appointment;
import com.patient.model.Patient;
import com.patient.repository.AppointmentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientService patientService;

    @Test
    public void testGetAppointmentsByPatientEmail() {
        
        String email = "test@example.com";
        List<Appointment> appointments = List.of(new Appointment(), new Appointment());
        when(appointmentRepository.findByPatientEmail(email)).thenReturn(appointments);

       
        List<Appointment> result = appointmentService.getAppointmentsByPatientEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(appointmentRepository, times(1)).findByPatientEmail(email);
    }

    @Test
    public void testBookAppointment() {
        
        String email = "test@example.com";
        Patient patient = new Patient();
        patient.setEmail(email);

        Appointment appointment = new Appointment();
        LocalDateTime beforeBookingTime = LocalDateTime.now();

        when(patientService.findByEmail(email)).thenReturn(patient);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        
        appointmentService.bookAppointment(appointment, email);

        // Assert
        assertNotNull(appointment.getPatient());
        assertEquals(patient, appointment.getPatient());
        assertNotNull(appointment.getCurrentDateTime());
        assertTrue(appointment.getCurrentDateTime().isAfter(beforeBookingTime));
        verify(patientService, times(1)).findByEmail(email);
        verify(appointmentRepository, times(1)).save(appointment);
    }
}
