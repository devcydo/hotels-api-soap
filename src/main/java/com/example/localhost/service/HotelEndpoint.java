package com.example.localhost.service;

import com.example.localhost.repository.HotelRepository;
import com.localhost.xml.hotels.GetHotelRequest;
import com.localhost.xml.hotels.GetHotelResponse;
import com.localhost.xml.hotels.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class HotelEndpoint {

    private static final String NAMESPACE_URI = "http://www.localhost.com/xml/hotels";

    private HotelRepository hotelRepository;

    public Hotel toXmlHotel(com.example.localhost.model.Hotel hotel){
        System.out.println("hotel:" + hotel);
        Hotel xmlHotel = new Hotel();
        xmlHotel.setId(hotel.getId());
        xmlHotel.setName(hotel.getName());
        xmlHotel.setAddress(hotel.getAddress());
        xmlHotel.setRating(hotel.getRating());

        return xmlHotel;
    }

    @Autowired
    public HotelEndpoint(HotelRepository hotelRepository){
        this.hotelRepository = hotelRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getHotelRequest")
    @ResponsePayload
    public GetHotelResponse getHotel(@RequestPayload GetHotelRequest request){
        GetHotelResponse response = new GetHotelResponse();
        response.setHotel(toXmlHotel(hotelRepository.findByName(request.getName())));

        return response;
    }
}
