package com.bus.model;




import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
  
    private int age;
    private String phoneNumber;
    private String email;
    private int bookingExperience;
    private int websiteExperience;
    private int serviceExperience;
    private String feedbackText;
    
    
    private LocalDateTime createdAt;
    
    
    //Getters and Setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getBookingExperience() {
		return bookingExperience;
	}
	public void setBookingExperience(int bookingExperience) {
		this.bookingExperience = bookingExperience;
	}
	public int getWebsiteExperience() {
		return websiteExperience;
	}
	public void setWebsiteExperience(int websiteExperience) {
		this.websiteExperience = websiteExperience;
	}
	public int getServiceExperience() {
		return serviceExperience;
	}
	public void setServiceExperience(int serviceExperience) {
		this.serviceExperience = serviceExperience;
	}
	public String getFeedbackText() {
		return feedbackText;
	}
	public void setFeedbackText(String feedbackText) {
		this.feedbackText = feedbackText;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

    
    
    
}

