package com.bus.config;

import com.bus.model.User;
import com.bus.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecurityConfigTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private SecurityConfig securityConfig;
  

    @Test
    public void testUserDetailsService_UserFound() {
        String email = "test@example.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        when(userService.findByEmail(email)).thenReturn(user);

        UserDetailsService userDetailsService = securityConfig.userDetailsService();
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertEquals(true, userDetails.isEnabled());
        assertEquals(true, userDetails.isAccountNonExpired());
        assertEquals(true, userDetails.isCredentialsNonExpired());
        assertEquals(true, userDetails.isAccountNonLocked());
        assertEquals(Collections.emptySet(), userDetails.getAuthorities()); // Change to emptySet
    }


    
    @Test
    public void testUserDetailsService_UserNotFound() {
        String email = "nonexistent@example.com";

        when(userService.findByEmail(email)).thenReturn(null);

        UserDetailsService userDetailsService = securityConfig.userDetailsService();

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        });
    }
}
