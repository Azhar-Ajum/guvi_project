package com.patient.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tablet name is mandatory")
    private String tabletName;

    @NotBlank(message = "Usage instructions are mandatory")
    private String whenToUse;

    @NotBlank(message = "Doctor name is mandatory")
    private String doctorName;

    @ManyToOne(fetch = FetchType.LAZY)
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
    public String getTabletName() {
        return tabletName;
    }
    public void setTabletName(String tabletName) {
        this.tabletName = tabletName;
    }
    public String getWhenToUse() {
        return whenToUse;
    }
    public void setWhenToUse(String whenToUse) {
        this.whenToUse = whenToUse;
    }
    public String getDoctorName() {
        return doctorName;
    }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
