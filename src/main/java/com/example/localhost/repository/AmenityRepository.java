package com.example.localhost.repository;

import com.example.localhost.model.Amenity;
import com.example.localhost.model.Hotel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AmenityRepository extends CrudRepository<Amenity, Long> {
    List<Amenity> findByHotel_Id(long id_hotel);

    Amenity findById(long id);

    Amenity save(Amenity amenity);

    void deleteById(long id);
}
