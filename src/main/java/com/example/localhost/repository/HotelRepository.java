package com.example.localhost.repository;

import com.example.localhost.model.Hotel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface HotelRepository extends CrudRepository<Hotel, Long> {
    Hotel findById(long id);

    List<Hotel> findAll();

    Hotel save(Hotel hotel);

    void deleteById(long id);

}
