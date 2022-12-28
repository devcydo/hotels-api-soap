package com.example.localhost.repository;

import com.example.localhost.model.Hotel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public interface HotelRepository extends CrudRepository<Hotel, Long> {
    Hotel findById(long id);

    Set<Hotel> findAll();

    Hotel save(Hotel hotel);

    void deleteById(long id);

}
