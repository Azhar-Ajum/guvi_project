package com.bus.repository;




import org.springframework.data.jpa.repository.JpaRepository;

import com.bus.model.Bus;

public interface BusRepository extends JpaRepository<Bus, Long> {
}

