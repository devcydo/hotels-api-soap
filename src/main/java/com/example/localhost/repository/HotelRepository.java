package com.example.localhost.repository;

import com.localhost.xml.hotels.Hotel;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class HotelRepository {
    private static final Map<String, Hotel> hotels = new HashMap<>();

    @PostConstruct
    public void initData(){
        Hotel hotel = new Hotel();
        hotel.setName("Hotel 1");
        hotel.setAddress("Street 1");
        hotel.setRating(1);
        hotels.put(hotel.getName(), hotel);

        hotel = new Hotel();
        hotel.setName("Hotel 2");
        hotel.setAddress("Street 2");
        hotel.setRating(2);
        hotels.put(hotel.getName(), hotel);
    }

    public Hotel findHotel(String name){
        return hotels.get(name);
    }
}
