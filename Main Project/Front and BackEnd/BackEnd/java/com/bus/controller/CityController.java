package com.bus.controller;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bus.model.Bus;
import com.bus.model.City;
import com.bus.model.Passenger;
import com.bus.model.User;
import com.bus.service.BusService;
import com.bus.service.CityService;
import com.bus.service.PassengerService;
import com.bus.service.UserService;

@Controller
public class CityController {
	@Autowired
	private CityService cityService;

	@Autowired
	private UserService userService;

	@Autowired
	private BusService busService;

	@Autowired
	private PassengerService passengerService;

	
	
	
	//-------------Booking Details/History display------------
	
	@GetMapping("/booking-details")
	public String showBookingDetails( Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
	    User user = userService.findByEmail(userEmail);
	    if (user != null) {
	        List<Passenger> passengers = passengerService.findByUser(user);

	       
	        List<Passenger> currentBookings = new ArrayList<>();
	        List<Passenger> bookingHistory = new ArrayList<>();

	        LocalDate now = LocalDate.now();

	        for (Passenger passenger : passengers) {
	            LocalDate bookingDateTime = passenger.getBus().getBookingDateTime();

	            if (bookingDateTime.isAfter(now)) {
	                currentBookings.add(passenger);
	            } else {
	                bookingHistory.add(passenger);
	            }
	        }
	        
	        Collections.sort(currentBookings, new Comparator<Passenger>() {
	            @Override
	            public int compare(Passenger p1, Passenger p2) {
	                return p1.getBus().getBookingDateTime().compareTo(p2.getBus().getBookingDateTime());
	            }
	        });
	        
	     // Sort booking history by bookingDateTime in descending order
	        Collections.sort(bookingHistory, new Comparator<Passenger>() {
	            @Override
	            public int compare(Passenger p1, Passenger p2) {
	                return p2.getBus().getBookingDateTime().compareTo(p1.getBus().getBookingDateTime());
	            }
	        });

	        
	        model.addAttribute("currentBookings", currentBookings);
	        model.addAttribute("bookingHistory", bookingHistory);
	        model.addAttribute("userName", user.getName());
	        model.addAttribute("userEmail", userEmail);

	        return "booking-details";
	    } else {
	        model.addAttribute("error", "User not found");
	        return "error";
	    }
	}


	// --------------------------bus-list------------------------------------------


	@PostMapping("/find-bus")
	public String findBus(@ModelAttribute City city, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
		
		User user = userService.findByEmail(userEmail);
		if (user != null) {
			city.setUser(user);
			cityService.saveCity(city);

			model.addAttribute("buses", getDummyBuses());
			model.addAttribute("userEmail", userEmail);

			model.addAttribute("cityId", city.getId());

			return "bus-list";
		} else {
			model.addAttribute("error", "User not found");
			return "profile"; // Return to profile page if user is not found
		}
	}

	// ----------------------------------passenger Deatils---------//
	
	@PostMapping("/book-bus")
	public String bookBus(@RequestParam String busName, @RequestParam String route, @RequestParam String arrivalTime,
			@RequestParam String departureTime, @RequestParam double price,           
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bookingDateTime,
			@RequestParam String userEmail, @RequestParam long cityId, Model model) {
		User user = userService.findByEmail(userEmail);

		if (user != null) {
			Bus bus = new Bus();
			bus.setBusName(busName);
			bus.setRoute(route);
			bus.setArrivalTime(arrivalTime);
			bus.setDepartureTime(departureTime);
			bus.setPrice(price);           
			bus.setBookingDateTime(bookingDateTime);
			bus.setUser(user);

			busService.saveBus(bus, cityId);

			model.addAttribute("userEmail", userEmail);
			model.addAttribute("busId", bus.getId());
			model.addAttribute("cityId", cityId);
			return "passenger-details";

		} else {
			return "bus-list"; // Return to bus list if user is not found
		}
	}

	// -------------------------------------------success/passenger-details---//
	

	
	
	@PostMapping("/confirm-booking")
	public String confirmBooking(@ModelAttribute Passenger passenger, 
			@RequestParam Long busId, @RequestParam Long cityId, Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
		User user = userService.findByEmail(userEmail);
		if (user != null) {
			// Generate e-ticket (you can customize this based on your requirements)
			String eTicket = generateETicket();

			Bus bus = busService.findById(busId);
			City city = cityService.findById(cityId);

			passenger.setUser(user);

			passenger.setBus(bus);
			passenger.setCity(city);

			passenger.seteTicket(eTicket); // Set generated e-ticket
			passengerService.savePassenger(passenger);

			model.addAttribute("eTicket", eTicket);
			model.addAttribute("userEmail", userEmail);

			return "/success"; // Redirect to success page
		} else {
			return "error"; // Handle error scenario
		}
	}

	@GetMapping("/passenger-details")
	public String showPassengerDetails(Model model,  @RequestParam Long busId,
			@RequestParam Long cityId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
		model.addAttribute("userEmail", userEmail);

		model.addAttribute("busId", busId);
		model.addAttribute("cityId", cityId);

		model.addAttribute("passenger", new Passenger());
		return "passenger-details";
	}

	

	private List<Bus> getDummyBuses() {
		List<Bus> buses = new ArrayList<>();
		buses.add(new Bus(null, "Parveen Travels", "Normal route ", "10:00 AM", "12:00 PM", null, 500.0, null));
		buses.add(new Bus(null, "KPN Travels", "normal route ", "01:00 PM", "03:00 PM", null, 600.0, null));
		buses.add(new Bus(null, "SRS Travels", " normal route", "04:00 PM", "06:00 PM", null, 550.0, null));
		buses.add(new Bus(null, "TNSTC", " normal route", "07:00 PM", "09:00 PM", null, 700.0, null));
		buses.add(new Bus(null, "SETC", "normal route ", "10:00 PM", "12:00 AM", null, 800.0, null));
		return buses;
	}

	private String generateETicket() {
		// Generate a random e-ticket number 
		return "ET" + (int) (Math.random() * 100000);
	}

}
