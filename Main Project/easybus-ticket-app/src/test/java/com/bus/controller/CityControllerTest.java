package com.bus.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;


import com.bus.model.Bus;
import com.bus.model.City;
import com.bus.model.Passenger;
import com.bus.model.User;
import com.bus.service.BusService;
import com.bus.service.CityService;
import com.bus.service.PassengerService;
import com.bus.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) 
class CityControllerTest {

    @Mock
    private CityService cityService;

    @Mock
    private UserService userService;

    @Mock
    private BusService busService;

    @Mock
    private PassengerService passengerService;

    @Mock
    private Model model;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CityController cityController;
    
  


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
    }
    
   

    @Test
    void testShowBookingDetails() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("test@example.com");

        User user = new User();
        user.setName("Test User");
        when(userService.findByEmail("test@example.com")).thenReturn(user);

        List<Passenger> passengers = new ArrayList<>();
        Bus bus = new Bus();
        bus.setBookingDateTime(LocalDate.now().plusDays(1));
        Passenger passenger = new Passenger();
        passenger.setBus(bus);
        passengers.add(passenger);
        when(passengerService.findByUser(user)).thenReturn(passengers);

        String viewName = cityController.showBookingDetails(model);
        assertEquals("booking-details", viewName);

        verify(model).addAttribute("currentBookings", passengers);
        verify(model).addAttribute("bookingHistory", Collections.emptyList());
        verify(model).addAttribute("userName", "Test User");
        verify(model).addAttribute("userEmail", "test@example.com");
    }

    @Test
    void testFindBus() {
        User user = new User();
        when(userService.findByEmail("test@example.com")).thenReturn(user);

        City city = new City();
        city.setId(1L);
        when(cityService.saveCity(any(City.class))).thenReturn(city);

        String viewName = cityController.findBus(city, model);
        assertEquals("bus-list", viewName);

        verify(cityService).saveCity(any(City.class));
        verify(model).addAttribute(eq("buses"), anyList());
        verify(model).addAttribute(eq("userEmail"), anyString());
        verify(model).addAttribute(eq("cityId"), anyLong());
    }
    
    
    


    
    @Test
    public void testBookBusSuccess() {
        
        String busName = "Bus1";
        String route = "Route1";
        String arrivalTime = "12:00";
        String departureTime = "15:00";
        double price = 100.0;
        LocalDate bookingDateTime = LocalDate.now();
        String userEmail = "user@example.com";
        long cityId = 1L;

        User user = new User();
        user.setEmail(userEmail);
        user.setId(1L);

        Bus bus = new Bus();
        bus.setBusName(busName);
        bus.setRoute(route);
        bus.setArrivalTime(arrivalTime);
        bus.setDepartureTime(departureTime);
        bus.setPrice(price);
        bus.setBookingDateTime(bookingDateTime);
        bus.setUser(user);

        when(userService.findByEmail(userEmail)).thenReturn(user);
        when(busService.saveBus(any(Bus.class), eq(cityId))).thenAnswer(invocation -> {
            Bus savedBus = invocation.getArgument(0);
            savedBus.setId(1L); 
            return savedBus;
        });

        
        String viewName = cityController.bookBus(busName, route, arrivalTime, departureTime, price, bookingDateTime, userEmail, cityId, model);

        // Assert
        assertEquals("passenger-details", viewName);

        verify(model).addAttribute("userEmail", userEmail);
        verify(model).addAttribute("busId", 1L); 
        verify(model).addAttribute("cityId", cityId);

        verifyNoMoreInteractions(model);
    }

    @Test
    public void testBookBusUserNotFound() {

        String busName = "Bus1";
        String route = "Route1";
        String arrivalTime = "12:00";
        String departureTime = "15:00";
        double price = 100.0;
        LocalDate bookingDateTime = LocalDate.now();
        String userEmail = "user@example.com";
        long cityId = 1L;

        when(userService.findByEmail(userEmail)).thenReturn(null);

        
        String viewName = cityController.bookBus(busName, route, arrivalTime, departureTime, price, bookingDateTime, userEmail, cityId, model);

        // Assert
        assertEquals("bus-list", viewName);
        verifyNoInteractions(model); 
    }







    @Test
    void testConfirmBooking() {
        User user = new User();
        when(userService.findByEmail("test@example.com")).thenReturn(user);

        Bus bus = new Bus();
        when(busService.findById(anyLong())).thenReturn(bus);

        City city = new City();
        when(cityService.findById(anyLong())).thenReturn(city);

        Passenger passenger = new Passenger();
        passenger.setBus(bus);
        passenger.setCity(city);

        String viewName = cityController.confirmBooking(passenger, 1L, 1L, model);
        assertEquals("/success", viewName);

        verify(passengerService).savePassenger(any(Passenger.class));
        verify(model).addAttribute(eq("eTicket"), anyString());
        verify(model).addAttribute(eq("userEmail"), anyString());
    }

    @Test
    void testShowPassengerDetails() {
        String viewName = cityController.showPassengerDetails(model, 1L, 1L);
        assertEquals("passenger-details", viewName);

        verify(model).addAttribute(eq("userEmail"), anyString());
        verify(model).addAttribute(eq("busId"), anyLong());
        verify(model).addAttribute(eq("cityId"), anyLong());
        verify(model).addAttribute(eq("passenger"), any(Passenger.class));
    }
}
