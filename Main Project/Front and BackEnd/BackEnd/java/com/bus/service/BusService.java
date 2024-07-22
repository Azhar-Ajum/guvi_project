package com.bus.service;




import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bus.model.Bus;
import com.bus.model.City;
import com.bus.repository.BusRepository;
import com.bus.repository.CityRepository;

@Service
public class BusService {
    @Autowired
    private BusRepository busRepository;
    
    @Autowired
    private CityRepository cityRepository;

    public Bus saveBus(Bus bus , Long cityId) {
    	
    	 City city = cityRepository.findById(cityId)
                 .orElseThrow(() -> new RuntimeException("City not found"));
         bus.setCity(city);
        return busRepository.save(bus);
    }
    
    
    
    
    
    public Bus getBusById(Long id) {
        Optional<Bus> bus = busRepository.findById(id);
        return bus.orElse(null);
    }
    
    public Bus findById(Long id) {
        return busRepository.findById(id).orElse(null);
    }
    
    
    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }
}

