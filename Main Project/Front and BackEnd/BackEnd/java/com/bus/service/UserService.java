package com.bus.service;




import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.bus.model.User;

import com.bus.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {


	
	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

  
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    
    
    public User saveUser(User user) {
    	 user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            // Check if the exception is caused by email or password constraint violation
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                org.hibernate.exception.ConstraintViolationException constraintViolationException =
                        (org.hibernate.exception.ConstraintViolationException) e.getCause();
                if (constraintViolationException.getConstraintName().contains("email")) {
                    throw new RuntimeException("Email already exists");
                }
            }
            throw new RuntimeException("This Mail Already EXists");
        }
    }
    
    
     public User findByEmail(String email) {
    	 System.out.println("Searching for user by email: " + email);
        return userRepository.findByEmail(email).orElse(null);
    }
    
    
     }
