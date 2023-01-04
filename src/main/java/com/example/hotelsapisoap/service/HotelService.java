package com.example.hotelsapisoap.service;

import com.example.hotelsapisoap.model.Hotel;
import org.springframework.data.domain.Page;

public interface HotelService {

    Hotel getById(long id);
    Page<Hotel> filterByName(int page, String name);
    Hotel save(Hotel hotel);
    Hotel addAmenity(long id_hotel, long id_amenity);
    Hotel removeAmenity(long id_hotel, long id_amenity);
    boolean deleteById(long id);
}
