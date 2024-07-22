package com.bus.service;

import com.bus.model.Passenger;
import com.bus.model.User;
import com.bus.repository.PassengerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerServiceTest {

    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private PassengerService passengerService;

    @Test
    void testSavePassenger() {
        Passenger passenger = new Passenger();
        passenger.setName("John Doe");

        // Mock the repository save method
        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);

        
        Passenger savedPassenger = passengerService.savePassenger(passenger);

        // Verify the results
        assertNotNull(savedPassenger);
        assertEquals("John Doe", savedPassenger.getName());
        verify(passengerRepository, times(1)).save(passenger);
    }

    @Test
    void testFindByUser() {
        User user = new User();
        user.setEmail("user@example.com");

        List<Passenger> passengers = new ArrayList<>();
        Passenger passenger1 = new Passenger();
        passenger1.setName("Passenger 1");
        Passenger passenger2 = new Passenger();
        passenger2.setName("Passenger 2");
        passengers.add(passenger1);
        passengers.add(passenger2);

       
        when(passengerRepository.findByUser(any(User.class))).thenReturn(passengers);

     
        List<Passenger> foundPassengers = passengerService.findByUser(user);

        // Verify the results
        assertNotNull(foundPassengers);
        assertEquals(2, foundPassengers.size());
        assertEquals("Passenger 1", foundPassengers.get(0).getName());
        assertEquals("Passenger 2", foundPassengers.get(1).getName());
        verify(passengerRepository, times(1)).findByUser(user);
    }
}
