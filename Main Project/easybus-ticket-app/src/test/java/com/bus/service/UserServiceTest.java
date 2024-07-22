package com.bus.service;

import com.bus.model.User;
import com.bus.repository.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testSaveUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        // Success case
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals("encodedPassword", savedUser.getPassword());
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(user);

        // Email already exists case
        when(userRepository.save(any(User.class))).thenThrow(
                new DataIntegrityViolationException("",
                        new ConstraintViolationException("", null, "email")
                )
        );

        RuntimeException emailExistsException = assertThrows(RuntimeException.class, () -> userService.saveUser(user));
        assertEquals("Email already exists", emailExistsException.getMessage());

        // Generic constraint violation case
        when(userRepository.save(any(User.class))).thenThrow(
                new DataIntegrityViolationException("",
                        new ConstraintViolationException("", null, "other_constraint")
                )
        );

        RuntimeException constraintViolationException = assertThrows(RuntimeException.class, () -> userService.saveUser(user));
        assertEquals("This Mail Already EXists", constraintViolationException.getMessage());
    }

    @Test
    void testFindByEmail() {
        User user = new User();
        user.setEmail("test@example.com");

        // Success case
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        User foundUser = userService.findByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");

        // Not found case
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        User notFoundUser = userService.findByEmail("test@example.com");

        assertNull(notFoundUser);
        verify(userRepository, times(2)).findByEmail("test@example.com");
    }
}