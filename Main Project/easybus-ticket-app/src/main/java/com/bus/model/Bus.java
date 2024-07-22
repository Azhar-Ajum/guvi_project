package com.bus.model;




import java.time.LocalDate;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


@Entity
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String busName;
    
    private String route;
    private String arrivalTime;
    private String departureTime;
    private LocalDate bookingDateTime;
    private double price;
    
    //---------------Mapping-----------//

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
    
    
    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL)
    private List<Passenger> passengers;
    
   
    
 
 // Getters and setters
    
	public List<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Bus() {
    }

    // Constructor with parameters
    public Bus(Long id, String busName,String route, String arrivalTime, String departureTime, LocalDate bookingDateTime, double price, User user) {
        this.id = id;
        this.busName = busName;
        this.route = route;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.bookingDateTime = bookingDateTime;
        this.price = price;
        this.user = user;
    }
    
    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public LocalDate getBookingDateTime() {
		return bookingDateTime;
	}

	public void setBookingDateTime(LocalDate bookingDateTime2) {
		this.bookingDateTime = bookingDateTime2;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

    
	
    
    
}

