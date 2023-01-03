package com.example.hotelsapisoap.repository;

import com.example.hotelsapisoap.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Hotel findById(long id);

    List<Hotel> findHotelsByNameIsLikeIgnoreCaseOrNameContainingIgnoreCase(String name1, String name2);

    List<Hotel> findAll();

    Hotel save(Hotel hotel);

    void deleteById(long id);

}
