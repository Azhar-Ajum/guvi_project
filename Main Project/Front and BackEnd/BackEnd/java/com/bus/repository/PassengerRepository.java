package com.bus.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bus.model.Passenger;
import com.bus.model.User;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
	
	 List<Passenger> findByUser(User user);
}

