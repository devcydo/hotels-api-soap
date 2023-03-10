package com.example.hotelsapisoap.repository;

import com.example.hotelsapisoap.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Page<Hotel> findHotelsByNameContainingIgnoreCase(String name, Pageable pageable);

}
