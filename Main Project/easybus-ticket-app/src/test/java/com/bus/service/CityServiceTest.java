package com.bus.service;

import com.bus.model.City;
import com.bus.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @Test
    void testSaveCity() {
        City city = new City();
        city.setFromCity("Test From City");
        city.setToCity("Test To City");

        when(cityRepository.save(any(City.class))).thenReturn(city);

        City savedCity = cityService.saveCity(city);

        assertNotNull(savedCity);
        assertEquals("Test From City", savedCity.getFromCity());
        assertEquals("Test To City", savedCity.getToCity());
        verify(cityRepository, times(1)).save(city);
    }

    @Test
    void testFindById() {
        City city = new City();
        city.setId(1L);
        city.setFromCity("Test From City");
        city.setToCity("Test To City");

        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(city));

        City foundCity = cityService.findById(1L);

        assertNotNull(foundCity);
        assertEquals(1L, foundCity.getId());
        assertEquals("Test From City", foundCity.getFromCity());
        assertEquals("Test To City", foundCity.getToCity());
        verify(cityRepository, times(1)).findById(1L);

        // Test not found case
        when(cityRepository.findById(anyLong())).thenReturn(Optional.empty());

        City notFoundCity = cityService.findById(1L);

        assertNull(notFoundCity);
        verify(cityRepository, times(2)).findById(1L);
    }

    @Test
    void testGetAllCities() {
        City city1 = new City();
        city1.setFromCity("From City 1");
        city1.setToCity("To City 1");

        City city2 = new City();
        city2.setFromCity("From City 2");
        city2.setToCity("To City 2");

        List<City> cities = Arrays.asList(city1, city2);

        when(cityRepository.findAll()).thenReturn(cities);

        List<City> foundCities = cityService.getAllCities();

        assertNotNull(foundCities);
        assertEquals(2, foundCities.size());
        assertEquals("From City 1", foundCities.get(0).getFromCity());
        assertEquals("To City 1", foundCities.get(0).getToCity());
        assertEquals("From City 2", foundCities.get(1).getFromCity());
        assertEquals("To City 2", foundCities.get(1).getToCity());
        verify(cityRepository, times(1)).findAll();
    }
}
