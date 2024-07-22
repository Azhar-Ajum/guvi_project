package com.bus.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bus.model.City;

import com.bus.repository.CityRepository;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    public City saveCity(City city) {
        return cityRepository.save(city);
    }
    
    public City findById(Long id) {
        return cityRepository.findById(id).orElse(null);
    }
    
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
    
    
}

