package com.patient.controller;

import com.patient.model.Patient;
import com.patient.service.PatientService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {

    @InjectMocks
    private PatientController patientController;

    @Mock
    private PatientService patientService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    private Patient patient;

    @BeforeEach
    public void setUp() {
        patient = new Patient();
        patient.setEmail("test@example.com");
        patient.setPassword("password");
        patient.setDateOfBirth(LocalDate.of(2000, 1, 1));
    }

    @Test
    public void testIndex() {
        String view = patientController.index();
        assertEquals("index", view);
    }

    @Test
    public void testLoginPage() {
        String view = patientController.loginPage();
        assertEquals("login", view);
    }

    @Test
    public void testRegisterPage() {
        String view = patientController.registerPage(model);
        assertEquals("register", view);
        verify(model, times(1)).addAttribute(anyString(), any(Patient.class));
    }
    
    
    
    
    @Test
    public void testRegisterPatient_Success() {
       
    	 when(bindingResult.hasErrors()).thenReturn(false);
    	    when(patientService.savePatient(any(Patient.class))).thenReturn(patient);

    	    String successView = patientController.registerPatient(patient, bindingResult, model);
    	    assertEquals("redirect:/login", successView);
    	    verify(patientService, times(1)).savePatient(any(Patient.class));
    }


    
    @Test
    public void testRegisterPatient_ValidationErrors() {
     
        when(bindingResult.hasErrors()).thenReturn(true);

        
        String validationErrorView = patientController.registerPatient(patient, bindingResult, model);

        // Assert
        assertEquals("register", validationErrorView);
        verify(patientService, times(0)).savePatient(any(Patient.class));
    }

    
    @Test
    public void testRegisterPatient_RuntimeException() {
        
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new RuntimeException("Email already exists")).when(patientService).savePatient(any(Patient.class));

        
        String runtimeExceptionView = patientController.registerPatient(patient, bindingResult, model);

        // Assert
        assertEquals("register", runtimeExceptionView);
        verify(model, times(1)).addAttribute("error", "Email already exists");
    }

    
    
    
    
    @Test
    public void testAuthenticatePatient() {
        
        String validEmail = "test@example.com";
        String validPassword = "password";
        Patient patient = new Patient();
        patient.setEmail(validEmail);
        patient.setPassword(validPassword);

        when(patientService.findByEmail(validEmail)).thenReturn(patient);

        // Mock the Model object
        Model model = Mockito.mock(Model.class);

       
        String successView = patientController.authenticatePatient(validEmail, validPassword, model);

        // Assert
        assertEquals("redirect:/profile?email=" + validEmail, successView);
        verify(patientService, times(1)).findByEmail(validEmail);
        verify(model, never()).addAttribute(eq("error"), anyString()); // Ensure no error attribute is added

        // Failure case: Invalid email or password
        String invalidEmail = "invalid@example.com";
        String invalidPassword = "wrongpassword";

        when(patientService.findByEmail(invalidEmail)).thenReturn(null);

        String failureView = patientController.authenticatePatient(invalidEmail, invalidPassword, model);

        assertEquals("login", failureView);
        verify(patientService, times(1)).findByEmail(invalidEmail);
        verify(model, times(1)).addAttribute("error", "Invalid email or password");
    }

    
    
    
   

    @Test
    public void testViewProfile() {
        when(patientService.findByEmail("test@example.com")).thenReturn(patient);

        String view = patientController.viewProfile("test@example.com", model);
        assertEquals("profile", view);
        verify(model, times(1)).addAttribute("patient", patient);
    }

    @Test
    public void testVerifyPatient() {
        // Success case
        when(patientService.findByEmail("test@example.com")).thenReturn(patient);

        String successView = patientController.verifyPatient("test@example.com", "2000-01-01", model);
        assertEquals("reset-password", successView);
        verify(model, times(1)).addAttribute("patient", patient);

        // Failure case: Invalid email or date of birth
        when(patientService.findByEmail("test@example.com")).thenReturn(null);

        String failureView = patientController.verifyPatient("test@example.com", "2000-01-01", model);
        assertEquals("forgot-password", failureView);
        verify(model, times(1)).addAttribute("error", "Invalid email or date of birth");
    }

    @Test
    public void testResetPassword() {
        // Success case
        when(patientService.findByEmail("test@example.com")).thenReturn(patient);

        String successView = patientController.resetPassword("test@example.com", "newpassword", model);
        assertEquals("redirect:/login", successView);
        verify(patientService, times(1)).savePatient(any(Patient.class));

        // Failure case: Invalid password
        when(patientService.findByEmail("test@example.com")).thenReturn(null);

        String failureView = patientController.resetPassword("test@example.com", "newpassword", model);
        assertEquals("reset-password", failureView);
        verify(model, times(1)).addAttribute("error", "Invalid password");
    }

    @Test
    public void testLogout() {
        String view = patientController.logout();
        assertEquals("redirect:/", view);
    }
}
