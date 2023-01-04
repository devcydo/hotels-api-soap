package com.example.hotelsapisoap.service;

import com.example.hotelsapisoap.model.Amenity;
import com.example.hotelsapisoap.model.Hotel;
import com.example.hotelsapisoap.repository.AmenityRepository;
import com.example.hotelsapisoap.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {
    private HotelRepository repository;
    private AmenityRepository amenityRepository;

    @Autowired
    public HotelServiceImpl(HotelRepository repository, AmenityRepository amenityRepository){
        this.repository = repository;
        this.amenityRepository = amenityRepository;
    }

    @Override
    public Hotel getById(long id){
        return repository.findById(id);
    };

    @Override
    public Page<Hotel> filterByName(int page, String name) {
        Pageable pageable = PageRequest.of(page, 5);

        if(!name.equals("")) {
            return repository.findHotelsByNameContainingIgnoreCase(name, pageable);
        }

        return repository.findAll(pageable);
    }

    public Hotel save(Hotel hotel) {
        try {
            return repository.save(hotel);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Hotel addAmenity(long id_hotel, long id_amenity) {
        try {
            Hotel hotel = repository.findById(id_hotel);

            Amenity amenity = amenityRepository.findById(id_amenity);

            hotel.addAmenity(amenity);
            return repository.save(hotel);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Hotel removeAmenity(long id_hotel, long id_amenity) {
        try {
            Hotel hotel = repository.findById(id_hotel);
            hotel.removeAmenity(id_amenity);
            return repository.save(hotel);
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
