package com.example.localhost.repository;

import com.example.localhost.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Hotel findByName(String name);
}
