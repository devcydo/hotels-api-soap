package com.example.hotelsapisoap.service;

import com.example.hotelsapisoap.model.Amenity;

import java.util.Set;

public interface AmenityService {

    Amenity getById(long id);
    Set<Amenity> getAmenities();
    Set<Amenity> getByHotel(long id_hotel);
    Amenity addAmenity(Amenity amenity);
    Amenity editAmenity(Amenity amenity);
    void deleteById(long id);
}
