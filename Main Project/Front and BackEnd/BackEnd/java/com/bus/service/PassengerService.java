package com.bus.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bus.model.Passenger;
import com.bus.model.User;
import com.bus.repository.PassengerRepository;

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    public Passenger savePassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }
    
    public List<Passenger> findByUser(User user) {
        return passengerRepository.findByUser(user);
}
}

