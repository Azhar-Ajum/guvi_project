package com.bus.controller;

import com.bus.model.City;
import com.bus.model.User;
import com.bus.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserController userController;

    @Test
    void testIndex() {
        String viewName = userController.index();
        assertEquals("index", viewName);
    }

    @Test
    void testLoginPage() {
        String viewName = userController.loginPage();
        assertEquals("login", viewName);
    }

    @Test
    void testRegisterPage() {
        when(model.addAttribute(anyString(), any())).thenReturn(model);
        String viewName = userController.registerPage(model);
        assertEquals("register", viewName);
        verify(model, times(1)).addAttribute(eq("user"), any(User.class));
    }

    @Test
    void testRegisterUserWithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String viewName = userController.registerUser(new User(), bindingResult, model);
        assertEquals("register", viewName);
    }


    
    @Test
    void testRegisterUserWithoutErrors() {
        when(bindingResult.hasErrors()).thenReturn(false);
        doAnswer(invocation -> null).when(userService).saveUser(any(User.class));
        String viewName = userController.registerUser(new User(), bindingResult, model);
        assertEquals("redirect:/login", viewName);
    }

    @Test
    void testRegisterUserException() {
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new RuntimeException("Error")).when(userService).saveUser(any(User.class));
        String viewName = userController.registerUser(new User(), bindingResult, model);
        assertEquals("register", viewName);
        verify(model, times(1)).addAttribute(eq("error"), eq("Error"));
    }

    @Test
    void testAuthenticateUser() {
        User user = new User();
        when(userService.findByEmail(anyString())).thenReturn(user);
        String viewName = userController.authenticateUser("test@example.com", model);
        assertEquals("redirect:/profile?email=test@example.com", viewName);
    }

    @Test
    void testShowProfile() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        user.setName("Test User");
        when(userService.findByEmail(anyString())).thenReturn(user);
        when(model.addAttribute(anyString(), any())).thenReturn(model);

        String viewName = userController.showProfile(model);
        assertEquals("profile", viewName);
        verify(model, times(1)).addAttribute(eq("user"), eq(user));
        verify(model, times(1)).addAttribute(eq("city"), any(City.class));
        verify(model, times(1)).addAttribute(eq("cities"), any());
        verify(model, times(1)).addAttribute(eq("userName"), eq("Test User"));
    }

    @Test
    void testShowProfileUserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(userService.findByEmail(anyString())).thenReturn(null);

        String viewName = userController.showProfile(model);
        assertEquals("redirect:/login", viewName);
        verify(model, times(1)).addAttribute(eq("error"), eq("User not found"));
    }

    @Test
    void testVerifyUser() {
        User user = new User();
        user.setDateOfBirth(LocalDate.of(2000, 1, 1));
        when(userService.findByEmail(anyString())).thenReturn(user);

        String viewName = userController.verifyUser("test@example.com", "2000-01-01", model);
        assertEquals("reset-password", viewName);
        verify(model, times(1)).addAttribute(eq("user"), eq(user));
    }

    @Test
    void testVerifyUserInvalid() {
        when(userService.findByEmail(anyString())).thenReturn(null);
        String viewName = userController.verifyUser("test@example.com", "2000-01-01", model);
        assertEquals("forgot-password", viewName);
        verify(model, times(1)).addAttribute(eq("error"), eq("Invalid email or date of birth"));
    }


    
    @Test
    void testResetPassword() {
        User user = new User();
        when(userService.findByEmail(anyString())).thenReturn(user);
        doAnswer(invocation -> null).when(userService).saveUser(any(User.class));

        String viewName = userController.resetPassword("test@example.com", "newPassword", model);
        assertEquals("redirect:/login", viewName);
    }

    @Test
    void testResetPasswordInvalid() {
        when(userService.findByEmail(anyString())).thenReturn(null);
        String viewName = userController.resetPassword("test@example.com", "newPassword", model);
        assertEquals("reset-password", viewName);
        verify(model, times(1)).addAttribute(eq("error"), eq("Invalid email"));
    }

    @Test
    void testShowProfileInfo() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        when(userService.findByEmail(anyString())).thenReturn(user);
        when(model.addAttribute(anyString(), any())).thenReturn(model);

        String viewName = userController.showProfileInfo(model);
        assertEquals("profile-info", viewName);
        verify(model, times(1)).addAttribute(eq("user"), eq(user));
        verify(model, times(1)).addAttribute(eq("userEmail"), eq("test@example.com"));
    }

    @Test
    void testShowProfileInfoUserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(userService.findByEmail(anyString())).thenReturn(null);

        String viewName = userController.showProfileInfo(model);
        assertEquals("redirect:/login", viewName);
        verify(model, times(1)).addAttribute(eq("error"), eq("User not found"));
    }

    @Test
    void testShowProfileEdit() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        when(userService.findByEmail(anyString())).thenReturn(user);
        when(model.addAttribute(anyString(), any())).thenReturn(model);

        String viewName = userController.showProfileEdit(model);
        assertEquals("profile-edit", viewName);
        verify(model, times(1)).addAttribute(eq("user"), eq(user));
        verify(model, times(1)).addAttribute(eq("userEmail"), eq("test@example.com"));
    }

    @Test
    void testShowProfileEditUserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(userService.findByEmail(anyString())).thenReturn(null);

        String viewName = userController.showProfileEdit(model);
        assertEquals("redirect:/login", viewName);
        verify(model, times(1)).addAttribute(eq("error"), eq("User not found"));
    }


    
    @Test
    void testSaveProfileEdit() {
        User user = new User();
        when(userService.findByEmail(anyString())).thenReturn(user);
        doAnswer(invocation -> null).when(userService).saveUser(any(User.class));

        String viewName = userController.saveProfileEdit("test@example.com", "New Name", 30, LocalDate.of(1990, 1, 1), model);
        assertEquals("redirect:/profile-info?userEmail=test@example.com", viewName);
    }

    @Test
    void testSaveProfileEditUserNotFound() {
        when(userService.findByEmail(anyString())).thenReturn(null);
        String viewName = userController.saveProfileEdit("test@example.com", "New Name", 30, LocalDate.of(1990, 1, 1), model);
        assertEquals("profile-edit", viewName);
        verify(model, times(1)).addAttribute(eq("error"), eq("User not found"));
    }
}