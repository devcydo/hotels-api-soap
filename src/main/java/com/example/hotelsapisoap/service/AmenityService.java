package com.example.hotelsapisoap.service;

import com.example.hotelsapisoap.model.Amenity;
import com.example.hotelsapisoap.repository.AmenityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AmenityService {
    private AmenityRepository repository;

    public AmenityService() {}

    @Autowired
    public AmenityService(AmenityRepository repository) {
        this.repository = repository;
    }

    public Amenity getById(long id){
        Amenity amenity = repository.findById(id);
        return amenity;
    };

    public Set<Amenity> getAll() { return repository.findAll(); }

    public Set<Amenity> getByHotel(long id_hotel) {
        return repository.findAmenitiesByHotelsId(id_hotel);
    }

    public Amenity save(Amenity amenity) {
        try {
            return repository.save(amenity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteById(long id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}