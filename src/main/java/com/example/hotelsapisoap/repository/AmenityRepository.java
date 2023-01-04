package com.example.hotelsapisoap.repository;

import com.example.hotelsapisoap.model.Amenity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public interface AmenityRepository extends CrudRepository<Amenity, Long> {
    Set<Amenity> findAll();
    Set<Amenity> findAmenitiesByHotelsId(long id_hotel);
}
