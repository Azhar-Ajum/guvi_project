package com.bus.service;

import com.bus.model.Bus;
import com.bus.model.City;
import com.bus.repository.BusRepository;
import com.bus.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusServiceTest {

    @Mock
    private BusRepository busRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private BusService busService;

    @Test
    void testSaveBus() {
        Bus bus = new Bus();
        bus.setBusName("Test Bus");
        bus.setRoute("Test Route");
        bus.setArrivalTime("10:00 AM");
        bus.setDepartureTime("12:00 PM");
        bus.setBookingDateTime(LocalDate.now());
        bus.setPrice(50.0);

        City city = new City();
        city.setId(1L);
        city.setFromCity("From City");
        city.setToCity("To City");

        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(city));
        when(busRepository.save(any(Bus.class))).thenReturn(bus);

        Bus savedBus = busService.saveBus(bus, 1L);

        assertNotNull(savedBus);
        assertEquals("Test Bus", savedBus.getBusName());
        assertEquals(city, savedBus.getCity());
        verify(cityRepository, times(1)).findById(1L);
        verify(busRepository, times(1)).save(bus);
    }

    @Test
    void testSaveBusCityNotFound() {
        Bus bus = new Bus();
        when(cityRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> busService.saveBus(bus, 1L));

        assertEquals("City not found", exception.getMessage());
        verify(cityRepository, times(1)).findById(1L);
        verify(busRepository, never()).save(any(Bus.class));
    }

    @Test
    void testGetBusById() {
        Bus bus = new Bus();
        bus.setId(1L);
        when(busRepository.findById(anyLong())).thenReturn(Optional.of(bus));

        Bus foundBus = busService.getBusById(1L);

        assertNotNull(foundBus);
        assertEquals(1L, foundBus.getId());
        verify(busRepository, times(1)).findById(1L);

        // Test not found case
        when(busRepository.findById(anyLong())).thenReturn(Optional.empty());

        Bus notFoundBus = busService.getBusById(1L);

        assertNull(notFoundBus);
        verify(busRepository, times(2)).findById(1L);
    }

    @Test
    void testFindById() {
        Bus bus = new Bus();
        bus.setId(1L);
        when(busRepository.findById(anyLong())).thenReturn(Optional.of(bus));

        Bus foundBus = busService.findById(1L);

        assertNotNull(foundBus);
        assertEquals(1L, foundBus.getId());
        verify(busRepository, times(1)).findById(1L);

        // Test not found case
        when(busRepository.findById(anyLong())).thenReturn(Optional.empty());

        Bus notFoundBus = busService.findById(1L);

        assertNull(notFoundBus);
        verify(busRepository, times(2)).findById(1L);
    }

    @Test
    void testGetAllBuses() {
        Bus bus1 = new Bus();
        bus1.setBusName("Bus 1");

        Bus bus2 = new Bus();
        bus2.setBusName("Bus 2");

        List<Bus> buses = Arrays.asList(bus1, bus2);

        when(busRepository.findAll()).thenReturn(buses);

        List<Bus> foundBuses = busService.getAllBuses();

        assertNotNull(foundBuses);
        assertEquals(2, foundBuses.size());
        assertEquals("Bus 1", foundBuses.get(0).getBusName());
        assertEquals("Bus 2", foundBuses.get(1).getBusName());
        verify(busRepository, times(1)).findAll();
    }
}
