package com.example.localhost.service;

import com.example.localhost.repository.HotelRepository;
import com.localhost.xml.hotels.GetHotelRequest;
import com.localhost.xml.hotels.GetHotelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class HotelEndpoint {

    private static final String NAMESPACE_URI = "http://www.localhost.com/xml/hotels";

    private HotelRepository hotelRepository;

    @Autowired
    public HotelEndpoint(HotelRepository hotelRepository){
        this.hotelRepository = hotelRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getHotelRequest")
    @ResponsePayload
    public GetHotelResponse getHotel(@RequestPayload GetHotelRequest request){
        System.out.println("Hi");
        GetHotelResponse response = new GetHotelResponse();
        response.setHotel(hotelRepository.findHotel(request.getName()));

        return response;
    }
}
