package com.example.localhost.service;

import com.example.localhost.model.Hotel;
import com.example.localhost.repository.HotelRepository;
import com.hotels.hotels.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class HotelEndpoint {

    private static final String NAMESPACE_URI = "http://hotels.com/hotels";

    private HotelRepository hotelRepository;

    @Autowired
    public HotelEndpoint(HotelRepository hotelRepository){
        this.hotelRepository = hotelRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetHotelDetailsRequest")
    @ResponsePayload
    public GetHotelDetailsResponse processHotelDetailsRequest(@RequestPayload GetHotelDetailsRequest request){
        Hotel hotel = hotelRepository.findById(request.getId());

        return mapHotelDetails(hotel);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllHotelDetailsRequest")
    @ResponsePayload
    public GetAllHotelDetailsResponse processAllHotelDetailsRequest(@RequestPayload GetAllHotelDetailsRequest request){
        List<Hotel> hotels = hotelRepository.findAll();

        return mapAllHotelDetails(hotels);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SaveHotelDetailsRequest")
    @ResponsePayload
    public SaveHotelDetailsResponse saveHotelDetailsRequest(@RequestPayload SaveHotelDetailsRequest request){
        Hotel hotel = hotelRepository.save(toHotel(request));

        return mapSaveHotelDetails(hotel);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteHotelDetailsRequest")
    @ResponsePayload
    public DeleteHotelDetailsResponse deleteHotelDetailsRequest(@RequestPayload DeleteHotelDetailsRequest request){
        hotelRepository.deleteById(request.getId());

        return mapDeleteHotelDetails("success");
    }

    private GetHotelDetailsResponse mapHotelDetails(Hotel hotel) {
        GetHotelDetailsResponse response = new GetHotelDetailsResponse();

        response.setHotelDetails(toHotelDetails(hotel));

        return response;
    }

    private GetAllHotelDetailsResponse mapAllHotelDetails(List<Hotel> hotels) {
        GetAllHotelDetailsResponse response = new GetAllHotelDetailsResponse();

        hotels.stream().forEach(hotel -> response.getHotelDetails().add(toHotelDetails(hotel)));

        return response;
    }

    private SaveHotelDetailsResponse mapSaveHotelDetails(Hotel hotel) {
        SaveHotelDetailsResponse response = new SaveHotelDetailsResponse();

        response.setHotelDetails(toHotelDetails(hotel));

        return response;
    }

    private DeleteHotelDetailsResponse mapDeleteHotelDetails(String status) {
        DeleteHotelDetailsResponse response = new DeleteHotelDetailsResponse();

        response.setStatus(status);

        return response;
    }

    private HotelDetails toHotelDetails(Hotel hotel) {
        HotelDetails hotelDetails = new HotelDetails();
        hotelDetails.setId(hotel.getId());
        hotelDetails.setName(hotel.getName());
        hotelDetails.setAddress(hotel.getAddress());
        hotelDetails.setRating(hotel.getRating());
        return hotelDetails;
    }

    private Hotel toHotel(SaveHotelDetailsRequest hotelDetails) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelDetails.getName());
        hotel.setAddress(hotelDetails.getAddress());
        hotel.setRating(hotelDetails.getRating());
        return hotel;
    }

}
