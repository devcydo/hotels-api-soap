package com.example.localhost.repository;

import com.example.localhost.model.Amenity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public interface AmenityRepository extends CrudRepository<Amenity, Long> {
    Set<Amenity> findByHotels(long id_hotel);

    Amenity findById(long id);

    Amenity save(Amenity amenity);

    void deleteById(long id);
}
