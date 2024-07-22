package com.bus.controller;

import com.bus.model.City;
import com.bus.model.User;
import com.bus.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

 

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
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.saveUser(user);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage()); // Add error message to the model
            return "register";
        }

        return "redirect:/login";
    }

    @PostMapping("/login")
    public String authenticateUser(@RequestParam String email, Model model) {
        User user = userService.findByEmail(email);
            model.addAttribute("user", user);
            return "redirect:/profile?email=" + email;
    }       

    
    
    

    
    @GetMapping("/profile")
    public String showProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userService.findByEmail(userEmail);

        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("city", new City());
            model.addAttribute("cities", getCities());
            model.addAttribute("userName", user.getName());
            return "profile";
        } else {
            model.addAttribute("error", "User not found");
            return "redirect:/login";
        }
    }
    
    
    
    
    
    

    
    private String[] getCities() {
		return new String[] { "Chennai", "Coimbatore", "Madurai", "Tiruchirappalli", "Salem", "Erode", "Tirunelveli",
				"Tiruppur", "Vellore", "Thoothukudi" };
	}



    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String verifyUser(@RequestParam String email, @RequestParam String dateOfBirth, Model model) {
    	 System.out.println("Attempting to find user with email: " + email + " and date of birth: " + dateOfBirth);
        User user = userService.findByEmail(email);
        if (user != null && user.getDateOfBirth().equals(LocalDate.parse(dateOfBirth))) {
            model.addAttribute("user", user);
            System.out.println("User verified successfully: " + user.getEmail());
            return "reset-password";
        } else {
            model.addAttribute("error", "Invalid email or date of birth");
            return "forgot-password";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email, @RequestParam String newPassword, Model model) {
    	System.out.println("Resetting password for email: " + email);
        User user = userService.findByEmail(email);
        if (user != null) {
        	System.out.println("User found: " + user.getEmail());

        	user.setPassword(newPassword);
            userService.saveUser(user);
            System.out.println("Password updated successfully for email: " + user.getEmail());
            return "redirect:/login";
        } else {
            model.addAttribute("error", "Invalid email");
            return "reset-password";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @GetMapping("/profile-info")
    public String showProfileInfo( Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userService.findByEmail(userEmail);
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("userEmail", userEmail);
            return "profile-info";
        } else {
            model.addAttribute("error", "User not found");
            return "redirect:/login";
        }
    }

    @GetMapping("/profile-edit")
    public String showProfileEdit( Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userService.findByEmail(userEmail);
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("userEmail", userEmail);
            return "profile-edit";
        } else {
            model.addAttribute("error", "User not found");
            return "redirect:/login";
        }
    }

    @PostMapping("/profile-edit")
    public String saveProfileEdit(@RequestParam String userEmail,
                                  @RequestParam String name,
                                  @RequestParam int age,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
                                  Model model) {
        User user = userService.findByEmail(userEmail);
        if (user != null) {
            user.setName(name);
            user.setAge(age);
            user.setDateOfBirth(dateOfBirth);
            userService.saveUser(user);
            model.addAttribute("userEmail", userEmail);
            return "redirect:/profile-info?userEmail=" + userEmail;
        } else {
            model.addAttribute("error", "User not found");
            return "profile-edit";
        }
    }

    
}