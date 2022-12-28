package com.example.localhost.endpoint;

import com.example.localhost.exception.NotFoundException;
import com.example.localhost.exception.ServiceFaultException;
import com.example.localhost.model.Amenity;
import com.example.localhost.model.Hotel;
import com.example.localhost.service.HotelService;

import com.hotels.hotels.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Endpoint
public class HotelEndpoint {

    private static final String NAMESPACE_URI = "http://hotels.com/hotels";

    private HotelService hotelService;

    @Autowired
    public HotelEndpoint(HotelService hotelService){
        this.hotelService = hotelService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetHotelDetailsRequest")
    @ResponsePayload
    public GetHotelDetailsResponse processHotelDetailsRequest(@RequestPayload GetHotelDetailsRequest request) throws NotFoundException {
        Hotel hotel = hotelService.getById(request.getId());

        if(hotel == null) throw new NotFoundException("Hotel with id " + request.getId() + "not found");

        return mapHotelDetails(toHotelDetails(hotel));
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllHotelDetailsRequest")
    @ResponsePayload
    public GetAllHotelDetailsResponse processAllHotelDetailsRequest(@RequestPayload GetAllHotelDetailsRequest request){
        Set<Hotel> hotels = hotelService.getAll();

        return mapAllHotelDetails(hotels);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SaveHotelDetailsRequest")
    @ResponsePayload
    public SaveHotelDetailsResponse saveHotelDetailsRequest(@RequestPayload SaveHotelDetailsRequest request){
        Hotel hotel = hotelService.save(toHotel(request));
        return mapSaveHotelDetails(hotel);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteHotelDetailsRequest")
    @ResponsePayload
    public DeleteHotelDetailsResponse deleteHotelDetailsRequest(@RequestPayload DeleteHotelDetailsRequest request) throws ServiceFaultException {
        boolean deleted = hotelService.deleteById(request.getId());

        return mapDeleteHotelDetails(deleted);
    }

    private GetHotelDetailsResponse mapHotelDetails(HotelDetails hotelDetails) {
        GetHotelDetailsResponse response = new GetHotelDetailsResponse();

        response.setHotelDetails(hotelDetails);

        return response;
    }

    private GetAllHotelDetailsResponse mapAllHotelDetails(Set<Hotel> hotels) {
        GetAllHotelDetailsResponse response = new GetAllHotelDetailsResponse();

        hotels.stream().forEach(hotel -> response.getHotelDetails().add(toHotelDetails(hotel)));

        return response;
    }

    private SaveHotelDetailsResponse mapSaveHotelDetails(Hotel hotel) {
        SaveHotelDetailsResponse response = new SaveHotelDetailsResponse();

        response.setHotelDetails(toHotelDetails(hotel));

        return response;
    }

    private DeleteHotelDetailsResponse mapDeleteHotelDetails(boolean deleted) {
        DeleteHotelDetailsResponse response = new DeleteHotelDetailsResponse();
        ServiceStatus serviceStatus = new ServiceStatus();

        if (deleted) {
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Content deleted successfully");
        } else {
            serviceStatus.setStatusCode("ERROR");
            serviceStatus.setMessage("Exception while deleting hotel");
            serviceStatus.setMessage("Exception while deleting hotel");
        }

        response.setServiceStatus(serviceStatus);

        return response;
    }

    private HotelDetails toHotelDetails(Hotel hotel) {
        HotelDetails hotelDetails = new HotelDetails();

        hotelDetails.setId(hotel.getId());
        hotelDetails.setName(hotel.getName());
        hotelDetails.setAddress(hotel.getAddress());
        hotelDetails.setRating(hotel.getRating());

        Optional.ofNullable(hotel.getAmenities())
                .orElseGet(Collections::emptySet)
                .stream().forEach(amenity -> hotelDetails.getAmenityDetails().add(toAmenityDetails(amenity)));

        return hotelDetails;
    }

    private AmenityDetails toAmenityDetails(Amenity amenity) {
        AmenityDetails amenityDetails = new AmenityDetails();

        amenityDetails.setId(amenity.getId());
        amenityDetails.setName(amenity.getName());
        amenityDetails.setDescription(amenity.getDescription());

        return amenityDetails;
    }

    private Hotel toHotel(SaveHotelDetailsRequest hotelDetails) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelDetails.getName());
        hotel.setAddress(hotelDetails.getAddress());
        hotel.setRating(hotelDetails.getRating());
        return hotel;
    }

}
