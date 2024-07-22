package com.bus.config;



import java.util.Collections;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.web.SecurityFilterChain;

import com.bus.model.User;
import com.bus.service.UserService;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	
	private final UserService userService;

	

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }
	
	

   

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/", "/register", "/forgot-password", "/reset-password", "/login",
                		"/logo3.png",
                        "/offer1.jpg",
                        "/offer2.jpg",
                        "/offer3.jpg",
                        "/offer4.jpg",
                        "/offer5.jpg",
                        "/offer6.jpg",
                        "/offer7.jpg",
                        "/Bus1.jpg",
                        "/bus2.jpg",
                        "/bus3.jpg",
                        "/bus4.jpeg",
                        "/bus5.jpeg",
                        "/bus6.jpeg",
                        "/bus7.jpeg",
                        "/bus8.jpeg",
                        "/bus9.jpeg",
                        "/bus10.jpg",
                        "/bus11.jpeg",
                        "/bus12.jpg",
                        "/bus13.jpeg",
                        "/bus14.jpeg",
                        "/bus15.jpg",
                        "/static/**" 
                	                     ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/profile", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )
        .csrf(csrf -> csrf.disable());
        return http.build();
    }

    

   
    
    
    
    
    @Bean
    UserDetailsService userDetailsService() {
        return email -> {
        	System.out.println("Attempting to find user with email: " + email);
            User user = userService.findByEmail(email);
            if (user == null) {
            	System.out.println("User not found: " + email);
                throw new UsernameNotFoundException("User not found");
            }
            return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                true, // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                Collections.emptyList() // authorities
            );
        };
    }
    
}
