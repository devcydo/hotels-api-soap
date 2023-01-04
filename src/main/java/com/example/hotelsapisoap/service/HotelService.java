package com.example.hotelsapisoap.service;

import com.example.hotelsapisoap.model.Hotel;
import org.springframework.data.domain.Page;

public interface HotelService {

    Hotel getById(long id);
    Page<Hotel> getHotels(int page, String name);
    Hotel addHotel(Hotel hotel);
    Hotel editHotel(Hotel hotel);
    Hotel addAmenity(long id_hotel, long id_amenity);
    Hotel removeAmenity(long id_hotel, long id_amenity);
    void deleteById(long id);
}
