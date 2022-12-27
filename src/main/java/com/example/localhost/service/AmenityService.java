package com.example.localhost.service;

import com.example.localhost.model.Amenity;
import com.example.localhost.model.Hotel;
import com.example.localhost.repository.AmenityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmenityService {
    private AmenityRepository repository;

    public AmenityService() {
    }

    @Autowired
    public AmenityService(AmenityRepository repository) {
        this.repository = repository;
    }

    public Amenity getById(long id){
        Amenity amenity = repository.findById(id);
        return amenity;
    };

    public List<Amenity> getByHotel(long id_hotel) {
        return repository.findByHotel_Id(id_hotel);
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
