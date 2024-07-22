package com.patient.model;

import jakarta.persistence.*;
//import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Doctor name is mandatory")
    private String doctorName;

    @NotBlank(message = "Specialist is mandatory")
    private String specialist;

    @NotNull(message = "Appointment date and time is mandatory")
    //@FutureOrPresent(message = "Appointment date and time must be in the present or future") // purpose fully made this validation under comment because i want to check booking history!
    private LocalDateTime appointmentDateTime;

    @NotNull(message = "Current date and time is mandatory")
    private LocalDateTime currentDateTime;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @NotNull(message = "Patient is mandatory")
    private Patient patient;

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDoctorName() {
        return doctorName;
    }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    public String getSpecialist() {
        return specialist;
    }
    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }
    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }
    public LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }
    public void setCurrentDateTime(LocalDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
