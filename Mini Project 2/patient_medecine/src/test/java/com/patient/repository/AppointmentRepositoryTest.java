package com.patient.repository;

import com.patient.model.Appointment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppointmentRepositoryTest {

    @Mock
    private AppointmentRepository appointmentRepository; // Mocking the interface

    @Test
    public void testFindByPatientEmail() {
       
        String email = "test@example.com";
        Appointment appointment1 = new Appointment();
        Appointment appointment2 = new Appointment();
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment1);
        appointments.add(appointment2);

        when(appointmentRepository.findByPatientEmail(email)).thenReturn(appointments);

       
        List<Appointment> foundAppointments = appointmentRepository.findByPatientEmail(email);

        // Assert
        assertEquals(2, foundAppointments.size());
    }
}
