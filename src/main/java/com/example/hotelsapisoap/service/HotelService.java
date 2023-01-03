package com.example.hotelsapisoap.service;

import com.example.hotelsapisoap.model.Hotel;

import java.util.List;

public interface HotelService {

    Hotel getById(long id);
    List<Hotel> getAll();
    List<Hotel> filterByName(String name);
    Hotel save(Hotel hotel);
    Hotel addAmenity(long id_hotel, long id_amenity);
    Hotel removeAmenity(long id_hotel, long id_amenity);
    boolean deleteById(long id);
}
