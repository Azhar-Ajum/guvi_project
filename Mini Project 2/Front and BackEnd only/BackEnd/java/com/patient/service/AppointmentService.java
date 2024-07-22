
package com.patient.service;

import com.patient.model.Appointment;
import com.patient.model.Patient;
import com.patient.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientService patientService;

    public List<Appointment> getAppointmentsByPatientEmail(String email) {
        return appointmentRepository.findByPatientEmail(email);
    }

    public void bookAppointment(Appointment appointment, String patientEmail) {
        // Retrieve the patient by email
        Patient patient = patientService.findByEmail(patientEmail);
        appointment.setPatient(patient);

        // Set current date and time
        appointment.setCurrentDateTime(LocalDateTime.now());

        appointmentRepository.save(appointment);
    }
}
